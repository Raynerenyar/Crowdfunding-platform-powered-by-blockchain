import { animate, state, style, transition, trigger } from '@angular/animations';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Project } from 'src/app/model/model';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';

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
  projects!: Project[]

  // total count of projects
  length!: number

  // first index
  first = 0

  // number of panels to show in the page
  rows = 6

  notifier$ = new Subject<boolean>()

  fadingLeft = true
  topRowProjects!: Project[]
  btmRowProjects!: Project[]

  constructor(private repoSvc: SqlRepositoryService, private msgSvc: PrimeMessageService, private router: Router, private cdr: ChangeDetectorRef, private bcSvc: BlockchainService) { }

  ngOnInit(): void {
    this.repoSvc.getProjectsWithPage(this.first, this.rows)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (projects: Project[]) => {
          this.projects = projects
          this.length = projects.length
          console.log(this.length)
          this.sliceArray(this.projects)
          console.log(this.projects)

          this.repoSvc.getCountProjects()
            .pipe(takeUntil(this.notifier$))
            .subscribe({
              next: (count) => {
                this.length = count
                this.cdr.detectChanges()
                console.log("count of all projects", count)
              },
              error: (error) => {
                this.msgSvc.generalErrorMethod(error);
              }
            })

          this.getRaisedAmount()

        },
        error: error => {
          this.msgSvc.generalErrorMethod(error)
        }
      });



  }
  onPageNumChange(event: { first: number; rows: number; }) {
    console.log(event.first, event.rows)
    this.repoSvc.getProjectsWithPage(event.first, event.rows)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (projects: Project[]) => {
          this.projects = projects
          this.sliceArray(this.projects)
          // assign the bool to change the animation base on going forwards or backwards 
          if (event.first < this.first) { this.fadingLeft = false }
          if (event.first > this.first) { this.fadingLeft = true }
          // event emits the next number
          this.first = event.first
          this.rows = event.rows

          // assign raisedAmount to each project
          this.getRaisedAmount()
        },
        error: error => {
          this.msgSvc.generalErrorMethod(error)
        }
      });

  }

  sliceArray(projects: Project[]) {
    length = projects.length

    if (length % 6 == 0) {
      this.topRowProjects = this.projects.slice(0, this.rows / 2)
      this.btmRowProjects = this.projects.slice(this.rows / 2, this.rows)
      return
    }
    if (length % 3 >= 1) {
      this.topRowProjects = this.projects.slice(0, this.rows / 2)
      this.btmRowProjects = this.projects.slice(this.rows / 2, length)
      return
    }
    if (length % 3 < 1) {
      this.topRowProjects = this.projects.slice(0, length)
      this.btmRowProjects = []
      return
    }
  }

  getRaisedAmount() {
    this.projects.forEach((project: Project) => {
      this.bcSvc.getRaisedAmount(project.projectAddress)
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: (value) => {
            project.raisedAmount = value
          },
          error: (err) => { }
        })

    });
  }

  goToProject(projectAddress: string) {
    // routerLink="/explore/{{project.projectAddress}}
    this.router.navigate(['explore', projectAddress])
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
