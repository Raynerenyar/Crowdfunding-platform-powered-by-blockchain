import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { NewComment } from 'src/app/model/model';

@Component({
  selector: 'app-project-comments',
  templateUrl: './project-comments.component.html',
  styleUrls: ['./project-comments.component.css']
})

export class ProjectCommentsComponent implements OnInit {

  notifier = new Subject<boolean>()
  projectAddress!: string | null
  comments!: NewComment[]

  constructor(private router: ActivatedRoute) { }

  ngOnInit(): void {
    this.router.paramMap
      .pipe(takeUntil(this.notifier))
      .subscribe({
        next: (param: ParamMap) => this.projectAddress = param.get('address'),
        error: (error: HttpErrorResponse) => console.log(error.message)
      })
  }
}


