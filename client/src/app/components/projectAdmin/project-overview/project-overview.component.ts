import { isPlatformBrowser } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnInit, Output, Renderer2, ViewChild } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { ScrollPanel } from 'primeng/scrollpanel';
import { Subject, takeUntil, timeout } from 'rxjs';
import { Project, Request } from 'src/app/model/model';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';

@Component({
  selector: 'app-project-overview',
  templateUrl: './project-overview.component.html',
  styleUrls: ['./project-overview.component.css']
})
export class ProjectOverviewComponent implements OnInit {

  requests!: Request[]
  notifier$ = new Subject<boolean>()
  project!: Project

  projectAddress!: string
  raisedAmount!: number

  requestEvent = new Subject<boolean>()

  constructor(private route: ActivatedRoute, private router: Router, private repoSvc: SqlRepositoryService, private renderer: Renderer2, private cdr: ChangeDetectorRef, private elementRef: ElementRef, private bcSvc: BlockchainService) { }

  ngOnInit(): void {
    if (!this.repoSvc.project) {
      this.router.navigate(['/project-admin'])
    } else {
      this.project = this.repoSvc.project
      this.projectAddress = this.project.projectAddress
      this.repoSvc.getRequests(this.projectAddress)
        .pipe(takeUntil(this.notifier$))
        .subscribe((requests) => {
          this.requests = requests

          this.bcSvc.getRaisedAmount(this.projectAddress)
            .pipe(takeUntil(this.notifier$))
            .subscribe({
              next: (value) => {
                this.raisedAmount = value
              },
              error: (err) => { }
            })
        })
    }
  }

  goBack() {
    this.router.navigate(['project-admin'], { replaceUrl: false });
  }

  goAnnouncement() {
    console.log("going to announcement")
    this.router.navigate(['project-admin', this.projectAddress, 'announcements']);
  }

  goComment() {
    console.log("going to comments")
    this.router.navigate(['project-admin', this.projectAddress, 'comments']);
  }

  goNewReq() {
    console.log("going to new requests")
    this.router.navigate(['project-admin', this.projectAddress, 'new-request']);
  }

  getRequest(num: number) {
    // this.router.navigate(['request', num])
    this.router.navigateByUrl(`project-admin/${this.projectAddress}/request/${num}`)
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
