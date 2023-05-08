import { AfterViewInit, ChangeDetectorRef, Component, Output } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ProjectDetails, RequestDetails } from 'src/app/model/model';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';

@Component({
  selector: 'app-project-overview',
  templateUrl: './project-overview.component.html',
  styleUrls: ['./project-overview.component.css']
})
export class ProjectOverviewComponent implements AfterViewInit {

  requests!: RequestDetails[]
  notifier$ = new Subject<boolean>()
  projectDetails!: ProjectDetails

  projectAddress!: string


  constructor(private route: ActivatedRoute, private repoSvc: SqlRepositoryService, private cdr: ChangeDetectorRef) {

  }
  ngAfterViewInit(): void {
    this.repoSvc.projectDetails
      .pipe(takeUntil(this.notifier$))
      .subscribe(
        (projectDetails) => {
          this.projectDetails = projectDetails
          this.cdr.detectChanges()
        }
      )

    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((param: ParamMap) => {
        let address = param.get('address')
        if (address) {
          this.repoSvc.emitProjectAddress(address)

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
