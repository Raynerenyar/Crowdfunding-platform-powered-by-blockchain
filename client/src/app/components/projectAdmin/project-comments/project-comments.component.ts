import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { NewComment } from 'src/app/model/model';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';

@Component({
  selector: 'app-project-comments',
  templateUrl: './project-comments.component.html',
  styleUrls: ['./project-comments.component.css']
})

export class ProjectCommentsComponent implements OnInit {

  projectAddress!: string | null
  comments!: NewComment[]
  notifier$ = new Subject<boolean>()
  first = 0
  rows = 10
  length = 0

  constructor(private route: ActivatedRoute, private router: Router, private mongoSvc: MongoRepoService) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (param: ParamMap) => {
          this.projectAddress = param.get('address')!
          this.getComments(this.projectAddress, this.first, this.rows)
          this.getCountComments(this.projectAddress)
        },
        error: (error: HttpErrorResponse) => console.log(error.message)
      })
  }

  goBack() {
    this.router.navigate(['project-admin', this.projectAddress])
  }

  getComments(projectAddress: string, offset: number, limit: number) {
    this.mongoSvc.getCommentsByPage(projectAddress, offset, limit)
      .pipe(takeUntil(this.notifier$))
      .subscribe((comments: NewComment[]) => {
        this.comments = comments
        // assign new first and rows value
        this.first = offset
        this.rows = limit
      })
  }

  getCountComments(projectAddress: string) {
    this.mongoSvc.countComments(projectAddress)
      .pipe(takeUntil(this.notifier$))
      .subscribe((count) => {
        this.length = count
      })
  }

  onPageNumChange(event: { first: number; rows: number; }) {
    this.getComments(this.projectAddress!, event.first, event.rows)
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}


