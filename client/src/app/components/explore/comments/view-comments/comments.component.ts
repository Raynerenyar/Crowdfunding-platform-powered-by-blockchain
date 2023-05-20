import { Component, Input, OnInit } from '@angular/core';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';
import { NewComment } from "../../../../model/model";
import { Subject, takeUntil } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {

  commentForm!: FormGroup
  projectAddress!: string
  comments!: NewComment[]
  charCount = 0
  charLimit = 2000
  notifier$ = new Subject<boolean>()

  constructor(private mongoSvc: MongoRepoService, private fb: FormBuilder, private storageSvc: SessionStorageService, private msgSvc: PrimeMessageService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((param: ParamMap) => {
        this.projectAddress = param.get('projectAddress')!
        this.mongoSvc.getComments(this.projectAddress)
          .pipe(takeUntil(this.notifier$))
          .subscribe({
            next: (comments: NewComment[]) => {
              if (comments)
                this.comments = comments.reverse()
            },
            error: (error: HttpErrorResponse) => { }
          })
      })

    this.commentForm = this.fb.group({
      comment: this.fb.control(null, [Validators.required, Validators.maxLength(this.charLimit)])
    })
  }

  goBack() {
    this.router.navigate(['explore', this.projectAddress])
  }

  postComment() {
    let posterAddress = this.storageSvc.getAddress()

    //  user's wallet must be connected
    if (posterAddress) {
      if (this.commentForm.valid) {
        let commentText = this.commentForm.get('comment')?.value
        let comment: NewComment = {
          body: commentText,
          datetimePosted: new Date(),
          posterAddress: posterAddress,
          projectAddress: this.projectAddress
        }
        this.mongoSvc.insertComment(comment)
          .pipe(takeUntil(this.notifier$))
          .subscribe({
            next: () => {

              // on successful insertion of comment
              window.location.reload()
            },
            error: () => {
              this.msgSvc.generalErrorMethod("Failed to post comment")
            }
          })
      }
    } else {
      this.msgSvc.generalErrorMethod("Your wallet is not connected!")
    }
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}