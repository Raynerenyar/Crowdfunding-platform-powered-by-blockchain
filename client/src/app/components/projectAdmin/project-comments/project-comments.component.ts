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

  notifier = new Subject<boolean>()
  projectAddress!: string | null
  comments!: NewComment[]
  notifier$ = new Subject<boolean>()

  constructor(private route: ActivatedRoute, private router: Router, private mongoSvc: MongoRepoService) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier))
      .subscribe({
        next: (param: ParamMap) => {
          this.projectAddress = param.get('address')!
          this.mongoSvc.getComments(this.projectAddress)
            .pipe(takeUntil(this.notifier$))
            .subscribe({
              next: (comments: NewComment[]) => {
                if (comments)
                  this.comments = comments.reverse()
              },
              error: (error: HttpErrorResponse) => { }
            })
        },
        error: (error: HttpErrorResponse) => console.log(error.message)
      })
  }

  goBack() {
    this.router.navigate(['project-admin', this.projectAddress])
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}


