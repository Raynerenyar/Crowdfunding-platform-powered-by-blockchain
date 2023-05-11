import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, ChangeDetectorRef, Component, OnInit, Output } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ProjectDetails, RequestDetails } from 'src/app/model/model';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';

@Component({
  selector: 'app-project-overview',
  templateUrl: './project-overview.component.html',
  styleUrls: ['./project-overview.component.css']
})
export class ProjectOverviewComponent implements AfterViewInit, OnInit {

  requests!: RequestDetails[]
  notifier$ = new Subject<boolean>()
  projectDetails!: ProjectDetails

  projectAddress!: string


  constructor(private route: ActivatedRoute, private repoSvc: SqlRepositoryService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.repoSvc.projectDetailsEvent
      .pipe(takeUntil(this.notifier$))
      .subscribe(
        (projectDetails) => {
          this.projectDetails = projectDetails
          console.log(projectDetails)
          this.cdr.detectChanges()
        }
      )
  }

  ngAfterViewInit(): void {

    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((param: ParamMap) => {


        let address = param.get('address')
        if (address) {
          this.projectAddress = address
          this.repoSvc.emitProjectAddress(address)
          let onNewProject = this.route.snapshot.queryParamMap.get('project')
          if (onNewProject) this.projectDetails = this.repoSvc.projectDetails
          this.repoSvc.getRequests(address)
            .pipe(takeUntil(this.notifier$))
            .subscribe((requests) => {
              this.requests = requests
            })
        }
      })


    /*         // get project details from database
        this.repoSvc.getProjects(address)
          .pipe(takeUntil(this.notifier$))
          .subscribe({
            next: (projectDetails: ProjectDetails) => {

            },
            error: (error) => {
              console.log(error)

            }
          }) */
  }
}
