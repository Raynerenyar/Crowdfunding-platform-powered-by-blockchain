import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, Input, OnDestroy, OnInit, ViewChild, Renderer2 } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ProjectDetails, RequestDetails } from 'src/app/model/model';
import { AlertMessageService } from 'src/app/services/alert.message.service';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { RepositoryService } from 'src/app/services/repository.service';
import { StorageService } from 'src/app/services/storage.service';
import { ScrollPanel } from 'primeng/scrollpanel';

@Component({
  selector: 'app-request',
  templateUrl: './request.component.html',
  styleUrls: ['./request.component.css']
})
export class RequestComponent implements OnInit, OnDestroy, AfterViewInit {

  @Input()
  requests!: RequestDetails[]
  @Input()
  project!: ProjectDetails
  @Input()
  tokenAddress!: string
  @Input()
  projectAddress!: string

  userAddress!: string
  tokenBalance!: number
  selectedRequestIndex!: number
  request!: RequestDetails

  notifier$ = new Subject<boolean>()

  @ViewChild('sp')
  scrollPanel!: ScrollPanel
  scrollPanelHeight = 800 // in px

  constructor(private route: ActivatedRoute, private repoSvc: RepositoryService, private msgSvc: AlertMessageService, private storageSvc: StorageService, private blockchainSvc: BlockchainService, private renderer: Renderer2) { }

  ngOnInit(): void {
    console.log("request loaded")
    this.userAddress = this.storageSvc.getAddress()
    console.log(this.tokenAddress)
    this.blockchainSvc.getTokenBalance(this.tokenAddress, this.userAddress)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: balance => {
          let balanceObj = balance as { tokenBalance: number }
          this.tokenBalance = balanceObj.tokenBalance
        },
        error: (error: HttpErrorResponse) => this.msgSvc.generalErrorMethod(error.message)
      })
  }

  ngAfterViewInit(): void {
    this.renderer.setStyle(this.scrollPanel, 'height', `${this.scrollPanelHeight}px`)
  }


  onChosenRequest(index: number) {
    this.request = this.requests[index]
    this.selectedRequestIndex = index
  }

  onContributeRequestInit(height: number) {
    if (height > this.scrollPanelHeight) this.renderer.setStyle(this.scrollPanel, 'height', `${height}px`)
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
