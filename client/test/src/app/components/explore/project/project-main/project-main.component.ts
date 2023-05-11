import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Subject, takeUntil } from 'rxjs';
import { ProjectDetails, RequestDetails } from 'src/app/model/model';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { RequestComponent } from '../../viewRequest/request.component';

@Component({
  selector: 'app-project-main',
  templateUrl: './project-main.component.html',
  styleUrls: ['./project-main.component.css']
})
export class ProjectMainComponent implements OnInit, OnDestroy {

  projectAddress!: string
  notifier$ = new Subject<boolean>()
  project!: ProjectDetails
  requests!: RequestDetails[]
  selectedRequestIndex!: number | undefined

  showProject = false
  showAnnouncement = false
  showComments = false
  showSingleRequest = false
  showNewComment = false

  items!: MenuItem[]
  activeItem!: MenuItem;

  @ViewChild(RequestComponent)
  requestCompo?: RequestComponent

  constructor(private route: ActivatedRoute, private repoSvc: SqlRepositoryService) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((params: ParamMap) => {
        this.projectAddress = params.get('projectAddress')!
        console.log(params.get('projectAddress'))
        this.repoSvc.getSingleProject(this.projectAddress)
          .pipe(takeUntil(this.notifier$))
          .subscribe({
            next: (project) => {
              this.project = project as ProjectDetails
              this.showProject = true
              this.repoSvc.getRequests(this.projectAddress)
                .pipe(takeUntil(this.notifier$))
                .subscribe({
                  next: (requests: RequestDetails[]) => {
                    this.requests = requests
                    console.log(requests)
                  },
                  error: (error: HttpErrorResponse) => {
                    console.log("not found")
                  }
                })
            },
            error: (error: HttpErrorResponse) => {
              console.log(error.status)
            }
          })
      })
    this.items = [
      { label: 'Project', icon: 'pi pi-user' },
      { label: 'Requests', icon: 'pi pi-wallet' },
      { label: 'Announcements', icon: 'pi pi-user', },
      { label: 'Comments', icon: 'pi pi-user' }
    ]
    this.activeItem = this.items[0];
  }

  onActiveItemChange($event: MenuItem) {
    console.log($event.label)
    this.selectView($event.label)
    this.selectedRequestIndex = undefined
  }

  onChosenRequest(index: number) {
    console.log(index)
    this.selectedRequestIndex = index
    this.selectView("Requests")
  }

  onNewComment() {
    this.selectView("New comments")
    console.log('on new comment')
    console.log(this.showNewComment)
    this.activeItem = {}
  }

  onCommentSubmission() {
    console.log("comment submission")
    // return back to comments tab
    this.onActiveItemChange({ label: 'Comments' })
    this.activeItem = this.items[3];
  }

  selectView(view: string | undefined) {
    this.showProject = (view === 'Project') ? true : false;
    this.showAnnouncement = (view === 'Announcements') ? true : false;
    this.showComments = (view === 'Comments') ? true : false;
    this.showSingleRequest = (view === 'Requests') ? true : false;
    this.showNewComment = (view === 'New comments') ? true : false;
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
