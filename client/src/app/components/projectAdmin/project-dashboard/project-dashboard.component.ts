import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { MegaMenuItem } from 'primeng/api';
import { Subject, takeUntil } from 'rxjs';
import { Announcement, Project, Request } from 'src/app/model/model';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { ProjectOverviewComponent } from '../project-overview/project-overview.component';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';
import { HttpErrorResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-project-dashboard',
  templateUrl: './project-dashboard.component.html',
  styleUrls: ['./project-dashboard.component.css']
})
export class ProjectDashboardComponent implements OnInit, OnDestroy {

  indexProject = 0
  indexRequests = 1
  indexAnnouncements = 2
  indexComments = 3

  items!: MegaMenuItem[];
  creatorAddress!: string;
  projects!: Project[]
  requests!: Request[]
  announcements!: Announcement[]

  completedCount = 0
  expiredCount = 0
  activeCount = 0

  notifier$ = new Subject<boolean>();

  constructor(
    private sqlRepoSvc: SqlRepositoryService,
    private storageSvc: SessionStorageService,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private bcSvc: BlockchainService,
    private msgSvc: PrimeMessageService,
    private walletSvc: WalletService
  ) { }

  ngOnInit() {
    this.creatorAddress = this.storageSvc.getAddress()

    // when entering project dashboard
    // get list of projects owned by this creator address
    this.sqlRepoSvc.getProjects(this.creatorAddress)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: projects => {

          if (projects != null) {
            this.projects = projects
            this.sqlRepoSvc.projects = projects
            this.projects.forEach((project: Project) => {

              if (project.completed) this.completedCount += 1
              if (project.expired) this.expiredCount += 1
              if (!project.completed && !project.expired) this.activeCount += 1

              // assign raisedAmount to each project retrieve from the blockchain
              this.bcSvc.getRaisedAmount(project.projectAddress)
                .pipe(takeUntil(this.notifier$))
                .subscribe({
                  next: (value) => { project.raisedAmount = value },
                  error: (err) => { }
                })
            });
          }
        },
        error: (error) => { console.log(error) }
      })
  }

  // private truncate(text: string) {
  //   if (text.length > 22) return text.substring(0, 19) + '...'
  //   return text
  // }

  goNewProject() {
    if (this.walletSvc.isOnRightChain()) {
      this.router.navigate(['project-admin', 'new-project'])
      return
    }
    this.msgSvc.tellToConnectToChain()
  }

  onCardClick($event: Event, index: number, projectAddress: string): void {
    let ele = $event.currentTarget as HTMLElement
    let tag = $event.target as HTMLElement
    if (tag.tagName != 'A') {
      ele.classList.add('click')
      this.sqlRepoSvc.project = this.sqlRepoSvc.projects[index]
      this.router.navigate(['project-admin', projectAddress])
    }
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
