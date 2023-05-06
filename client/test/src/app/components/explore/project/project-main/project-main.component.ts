import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Subject, takeUntil } from 'rxjs';
import { ProjectDetails, RequestDetails } from 'src/app/model/model';
import { RepositoryService } from 'src/app/services/repository.service';
import { RequestComponent } from '../../viewRequest/request.component';

@Component({
  selector: 'app-project-main',
  templateUrl: './project-main.component.html',
  styleUrls: ['./project-main.component.css']
})
export class ProjectMainComponent implements OnInit {

  projectAddress!: string
  notifier$ = new Subject<boolean>()
  project!: ProjectDetails
  requests!: RequestDetails[]
  selectedRequestIndex!: number | undefined

  showProject = false
  showAnnouncement = false
  showComments = false
  showSingleRequest = false

  items!: MenuItem[]
  activeItem!: MenuItem;

  @ViewChild(RequestComponent)
  requestCompo?: RequestComponent

  constructor(private route: ActivatedRoute, private repoSvc: RepositoryService) { }

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
      // routerLink: '/explore/' + this.projectAddress + '/project'
      { label: 'Project', icon: 'pi pi-user' },
      { label: 'Requests', icon: 'pi pi-wallet' },
      { label: 'Announcements', icon: 'pi pi-user', },
      { label: 'Comments', icon: 'pi pi-user' }
    ]
    this.activeItem = this.items[0];
  }

  onActiveItemChange($event: MenuItem) {
    console.log($event.label)
    switch ($event.label) {
      case "Project":
        this.showProject = true
        this.showAnnouncement = false
        this.showComments = false
        this.showSingleRequest = false
        break;
      case "Announcements":
        this.showProject = false
        this.showAnnouncement = true
        this.showComments = false
        this.showSingleRequest = false
        break
      case "Comments":
        this.showProject = false
        this.showAnnouncement = false
        this.showComments = true
        this.showSingleRequest = false
        break
      case "Requests":
        this.showProject = false
        this.showAnnouncement = false
        this.showComments = false
        this.showSingleRequest = true
        break
      default:
        this.showProject = true
        this.showAnnouncement = false
        this.showComments = false
        this.showSingleRequest = false
        break;
    }
    this.selectedRequestIndex = undefined
  }

  onChosenRequest(index: number) {
    console.log(index)
    this.selectedRequestIndex = index
    this.showProject = false
    this.showAnnouncement = false
    this.showComments = false
    this.showSingleRequest = true
  }
}
