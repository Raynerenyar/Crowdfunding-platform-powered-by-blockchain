import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { ProjectDetails } from 'src/app/model/model';
import { AlertMessageService } from 'src/app/services/alert.message.service';
import { RepositoryService } from 'src/app/services/repository.service';

@Component({
  selector: 'app-explore-projects',
  templateUrl: './explore-projects.component.html',
  styleUrls: ['./explore-projects.component.css'],
  // animations: [
  //   trigger('fade', [
  //     state('void', style({ opacity: 0 })),
  //     transition('void => *', [
  //       animate(1000)
  //     ]),
  //     transition('* => void', [
  //       animate(1000)
  //     ])

  //   ])
  // ]
})
export class ExploreProjectsComponent implements OnInit {
  projects!: ProjectDetails[]

  // total count of projects
  length!: number

  // first index
  first = 0

  // number of panels to show in the page
  rows = 6

  notifier$ = new Subject<boolean>()

  fadingLeft = true

  constructor(private repoSvc: RepositoryService, private msgSvc: AlertMessageService) { }

  ngOnInit(): void {
    this.repoSvc.getProjectsWithPage(this.first, this.rows)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (projects: ProjectDetails[]) => {
          this.projects = projects
          this.length = projects.length
        },
        error: error => {
          this.msgSvc.generalErrorMethod(error)
        }
      });

    this.repoSvc.getCountProjects()
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (count) => {
          this.length = count
        },
        error: (error) => {
          this.msgSvc.generalErrorMethod(error);
        }
      })

  }
  onPageNumChange(event: { first: number; rows: number; }) {

    this.repoSvc.getProjectsWithPage(event.first, event.rows)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (projects: ProjectDetails[]) => {
          this.projects = projects

          // assign the bool to change the animation base on going forwards or backwards 
          if (event.first < this.first) { this.fadingLeft = false }
          if (event.first > this.first) { this.fadingLeft = true }
          // event emits the next number
          this.first = event.first
          this.rows = event.rows

        },
        error: error => {
          this.msgSvc.generalErrorMethod(error)
        }
      });

  }

}
