import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ProjectDetails, RequestDetails } from 'src/app/model/model';

@Component({
  selector: 'app-project-body',
  templateUrl: './project-body.component.html',
  styleUrls: ['./project-body.component.css']
})
export class ProjectBodyComponent implements OnInit {

  @Input()
  projectAddress!: string

  notifier$ = new Subject<boolean>()
  @Input()
  project!: ProjectDetails
  @Input()
  requests!: RequestDetails[]

  constructor() { }

  ngOnInit(): void {

  }
}
