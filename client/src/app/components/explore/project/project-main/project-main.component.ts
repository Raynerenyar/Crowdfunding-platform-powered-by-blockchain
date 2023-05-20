import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Subject, takeUntil } from 'rxjs';
import { Project, Request } from 'src/app/model/model';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { RequestListComponent } from '../../viewRequest/request.list.component';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { AuthService } from 'src/app/services/auth.service';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-project-main',
  templateUrl: './project-main.component.html',
  styleUrls: ['./project-main.component.css']
})
export class ProjectMainComponent implements OnInit, OnDestroy {

  projectAddress!: string
  notifier$ = new Subject<boolean>()
  project!: Project
  requests!: Request[]
  userAddress!: string
  tokenBalance!: number

  selectedRequestIndex!: number | undefined

  contributeForm!: FormGroup

  showContribute = true
  showRefund = false

  items!: MenuItem[]
  activeItem!: MenuItem;

  @ViewChild(RequestListComponent)
  requestCompo?: RequestListComponent

  constructor(
    private route: ActivatedRoute,
    private repoSvc: SqlRepositoryService,
    private router: Router,
    private bcSvc: BlockchainService,
    private storageSvc: SessionStorageService,
    private fb: FormBuilder,
    private msgSvc: PrimeMessageService,
    private authSvc: AuthService,
    private walletSvc: WalletService) { }

  ngOnInit(): void {
    this.userAddress = this.storageSvc.getAddress()
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((params: ParamMap) => {
        this.projectAddress = params.get('projectAddress')!

        // get project
        this.repoSvc.getSingleProject(this.projectAddress)
          .pipe(takeUntil(this.notifier$))
          .subscribe({
            next: (project) => {
              this.project = project as Project

              /* assign to repo service project property so that
               * request page can retrieve it 
               */
              this.repoSvc.project = project

              this.contributeForm = this.fb.group({
                amount: this.fb.control(null, [Validators.required])
              })

              // get requests
              this.repoSvc.getRequests(this.projectAddress)
                .pipe(takeUntil(this.notifier$))
                .subscribe({
                  next: (requests: Request[]) => {
                    this.requests = requests
                    // console.log(requests)
                    this.project.numOfRequests = requests.length
                  },
                  error: (error: HttpErrorResponse) => {
                    console.log("not found")
                  }
                })

              // get balance of wallet that is connected
              if (this.userAddress) {
                this.bcSvc.getBalanceOf(this.userAddress, this.project.acceptingToken)
                  .then(balance => {
                    this.tokenBalance = balance
                  })
                  .catch()
              }

              // assign raisedAmount to project
              this.bcSvc.getRaisedAmount(this.project.projectAddress)
                .pipe(takeUntil(this.notifier$))
                .subscribe({
                  next: (value) => {
                    this.project.raisedAmount = value
                  },
                  error: (err) => { }
                })
            },
            error: (error: HttpErrorResponse) => {
              console.log(error.status)
            }
          })
      })
    // this.items = [
    //   { label: 'Project', icon: 'pi pi-user' },
    //   { label: 'Requests', icon: 'pi pi-wallet' },
    //   { label: 'Announcements', icon: 'pi pi-user', },
    //   { label: 'Comments', icon: 'pi pi-user' }
    // ]
    // this.activeItem = this.items[0];
  }

  // onActiveItemChange($event: MenuItem) {
  //   console.log($event.label)
  //   this.selectView($event.label)
  //   this.selectedRequestIndex = undefined
  // }

  // onMenuClicked(menu: string) {
  //   console.log(menu)
  //   this.selectView(menu)
  // }

  // onChosenRequest(index: number) {
  //   console.log(index)
  //   this.selectedRequestIndex = index
  //   this.selectView("Requests")
  // }

  // onNewComment() {
  //   this.selectView("New comments")
  //   console.log('on new comment')
  //   console.log(this.showNewComment)
  //   this.activeItem = {}
  // }

  // onCommentSubmission() {
  //   console.log("comment submission")
  //   // return back to comments tab
  //   this.onActiveItemChange({ label: 'Comments' })
  //   this.activeItem = this.items[3];
  // }

  // selectView(view: string | undefined) {
  //   this.showProject = (view === 'Project') ? true : false;
  //   this.showAnnouncement = (view === 'Announcements') ? true : false;
  //   this.showComments = (view === 'Comments') ? true : false;
  //   this.showSingleRequest = (view === 'Requests') ? true : false;
  //   this.showNewComment = (view === 'New comments') ? true : false;
  // }

  contributeTab() {
    this.showContribute = true
    this.showRefund = false
  }

  refundTab() {
    this.showContribute = false
    this.showRefund = true
  }

  goBack() {
    this.router.navigate(['explore'])
  }

  goAnnouncement() {
    this.router.navigate(['explore', this.projectAddress, 'announcements'])
  }

  goComment() {
    this.router.navigate(['explore', this.projectAddress, 'comments'])
  }

  getRequest(requestId: number) {
    this.router.navigate(['explore', this.projectAddress, 'request', requestId])
  }

  onContribute() {

    if (this.walletSvc.isOnRightChain()) {

      let walletAddress = this.storageSvc.getAddress()
      let contributeAmount = this.contributeForm.get('amount')?.value

      if (this.contributeForm.valid) {
        // approve token before contributing
        this.bcSvc.approveToken(this.project.acceptingToken, this.projectAddress, walletAddress, contributeAmount).then(
          () => {
            this.msgSvc.generalSuccessMethod("You have successfully approved")

            // on successful approval, send contribute transaction
            this.bcSvc.contribute(this.projectAddress, contributeAmount)
              .pipe(takeUntil(this.notifier$))
              .subscribe({
                next: () => {
                  this.msgSvc.generalSuccessMethod("You have successfully contributed.")
                  this.authSvc.reloadPage();
                },
                error: () => {
                  this.msgSvc.detailedErrorMethod("Execution reverted", "Minimum contribution is 100.")
                }
              })
          })
        return
      }
      this.msgSvc.generalInfoMethod("You have to enter an amount to contribute")

    } else this.msgSvc.tellToConnectToChain()

  }

  // TODO:
  onRefund() {
    if (this.walletSvc.isOnRightChain()) {

    } else this.msgSvc.tellToConnectToChain()


  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
