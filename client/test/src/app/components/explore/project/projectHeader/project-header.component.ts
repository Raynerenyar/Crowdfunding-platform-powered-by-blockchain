import { HttpErrorResponse } from '@angular/common/http';
import { AfterContentInit, AfterViewInit, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Project, Request } from 'src/app/model/model';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';

@Component({
  selector: 'app-project-header',
  templateUrl: './project-header.component.html',
  styleUrls: ['./project-header.component.css']
})
export class ProjectHeaderComponent implements OnInit, AfterViewInit, AfterContentInit, OnDestroy {

  @Input()
  projectAddress!: string
  // notifier$ = new Subject<boolean>()

  project!: Project

  days!: number

  notifier$ = new Subject<boolean>()

  constructor(private route: ActivatedRoute, private repoSvc: SqlRepositoryService) {
    console.log(this.project)
  }

  ngOnInit(): void {
    this.repoSvc.projectDetailsEvent
      .pipe(takeUntil(this.notifier$))
      .subscribe((project: Project) => {
        this.project = project
        console.log(this.project)
        let startDate = new Date()
        let endDate = new Date(this.project.deadline)
        const days = Math.floor(
          Math.abs(
            endDate.getTime() - startDate.getTime()
          ) / (1000 * 3600 * 24)
        )
        this.days = days
        console.log(days)
      })
  }

  ngAfterViewInit(): void {
  }

  ngAfterContentInit(): void {


  }
  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
