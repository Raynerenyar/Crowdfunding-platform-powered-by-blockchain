import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { signOut } from '@angular/fire/auth';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { MegaMenuItem } from 'primeng/api';
import { Subject, takeUntil } from 'rxjs';
import { Announcement, ProjectDetails, RequestDetails } from 'src/app/model/model';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { ProjectOverviewComponent } from '../project-overview/project-overview.component';
import { DexieDBService } from 'src/app/services/dexie-db.service';
import { liveQuery } from 'dexie';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';
import { HttpErrorResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-project-dashboard',
  templateUrl: './project-dashboard.component.html',
  styleUrls: ['./project-dashboard.component.css']
})
export class ProjectDashboardComponent implements OnInit, OnDestroy, AfterViewInit {
  itemz: MegaMenuItem[] = [
    {
      label: 'Projects', icon: 'tooth-icon', styleClass: 'font-normal', items: [[
        { label: 'Kickstart', items: [{ label: 'New Project', routerLink: ['new-project'] }] },
        { label: 'Current Projects', items: [] }
      ]]
    },
    {
      label: 'Requests', icon: 'pi pi-fw pi-calendar', disabled: true, items: [[
        { label: 'Request', items: [{ label: 'New Funding', routerLink: [] }] },
        { label: 'Current Requests', items: [] }
      ]]
    },
    {
      label: 'Announcements', icon: 'pi pi-fw pi-pencil', disabled: true, items: [[
        { label: 'Announce', items: [{ label: 'New Announcement', routerLink: [] }] },
        { label: 'Current', items: [{ label: 'Announcements', routerLink: [], disabled: true }] }
      ]]
    },
    { label: 'Comments', icon: 'pi pi-fw pi-file', disabled: false },
  ]
  item: MenuItem = { label: '', routerLink: '' }
  indexProject = 0
  indexRequests = 1
  indexAnnouncements = 2
  indexComments = 3

  items!: MegaMenuItem[];
  creatorAddress!: string;
  projects = new Map()
  requests!: RequestDetails[]
  announcements!: Announcement[]
  notifier$ = new Subject<boolean>();

  constructor(
    private sqlRepoSvc: SqlRepositoryService,
    private mongoRepoSvc: MongoRepoService,
    private storageSvc: SessionStorageService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private dexie: DexieDBService,
  ) { }

  ngOnInit() {
    // clear dexie
    this.dexie.delete()
  }

  ngAfterViewInit(): void {
    this.creatorAddress = this.storageSvc.getAddress()

    // when entering project dashboard
    // get list of projects owned by this creator address
    this.sqlRepoSvc.getProjects(this.creatorAddress)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: projects => {
          // this.projects = projects
          // console.log(this.projects)
          if (projects != null) {
            projects.forEach((projectDetails: ProjectDetails) => {
              this.projects.set(projectDetails.projectAddress, projectDetails)
              // push into megamenu
              this.itemz[this.indexProject].items![0][1].items?.push({
                label: this.truncate(projectDetails.title),
                routerLink: [projectDetails.projectAddress]
              })
            });
          }
        },
        error: (error) => { console.log(error) }
      })

    // when project is selected
    // show list of requests if available
    this.sqlRepoSvc.projectAddressEvent
      .pipe(takeUntil(this.notifier$))
      .subscribe((projectAddress) => {
        // transferring data to display on project details component
        this.sqlRepoSvc.emitProjectDetails(this.projects.get(projectAddress))

        // enable requests menu
        this.itemz[this.indexRequests].disabled = false

        // enabling new request option after getting project address
        this.itemz[this.indexRequests].items![0][0].disabled = false

        // assigning router link to new requests
        this.itemz[this.indexRequests].items![0][0].items![0].routerLink = [projectAddress, 'new-request']
        this.cdr.detectChanges()

        // getting list of requests from database
        this.sqlRepoSvc.getRequests(projectAddress)
          .pipe(takeUntil(this.notifier$))
          .subscribe({
            next: (requests: RequestDetails[]) => {
              console.log(requests)
              if (requests) {
                this.itemz[this.indexRequests].items![0][1].items = []
                requests.forEach((request: RequestDetails) => {

                  // assigning router link to each requests
                  this.itemz[this.indexRequests].items![0][1].items?.push({
                    label: this.truncate(request.title),
                    routerLink: [projectAddress, request.requestId]
                  })
                  // deleting db causes it to close, therefore reopen it to add requests
                  this.dexie.open()
                  let requestId
                  this.dexie.requests.get(request.requestId).then((_requestId) => requestId = _requestId)
                  console.log(requestId)
                  if (!requestId) {
                    this.dexie.requests.add(request)
                  }
                });
              }
            },
            error: (error) => console.log(error)
          })


        // get 
        this.itemz[this.indexAnnouncements].items![0][1].items![0].routerLink = [projectAddress, 'announcements']
        this.itemz[this.indexAnnouncements].items![0][1].items![0].disabled = false
        this.itemz[this.indexAnnouncements].items![0][0].items![0].routerLink = [projectAddress, 'new-announcement']
        this.itemz[this.indexAnnouncements].disabled = false
      })
  }

  private truncate(text: string) {
    if (text.length > 22) return text.substring(0, 19) + '...'
    return text
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
