import { Component, Input, OnInit } from '@angular/core';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';
import { NewComment } from "../../../../model/model";
import { Subject, takeUntil } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {

  @Input()
  projectAddress!: string
  comments!: NewComment[]
  notifier$ = new Subject<boolean>()

  constructor(private mongoSvc: MongoRepoService) { }

  ngOnInit(): void {
    this.mongoSvc.getComments(this.projectAddress)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (comments: NewComment[]) => this.comments = comments,
        error: (error: HttpErrorResponse) => { }
      })
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}