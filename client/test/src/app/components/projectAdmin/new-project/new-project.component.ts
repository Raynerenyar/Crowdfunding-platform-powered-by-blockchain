import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ProjectDetails } from 'src/app/model/model';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';

@Component({
  selector: 'app-new-project',
  templateUrl: './new-project.component.html',
  styleUrls: ['./new-project.component.css']
})
export class NewProjectComponent implements OnInit, OnDestroy {

  newProjectForm!: FormGroup
  charCount!: number
  creatorAddress!: string

  notifier$ = new Subject<boolean>()

  constructor(private fb: FormBuilder, private bcSvc: BlockchainService, private router: Router, private repoSvc: SqlRepositoryService, private storageSvc: SessionStorageService) { }

  ngOnInit(): void {
    this.newProjectForm = this.fb.group({
      title: this.fb.control<string>('', [Validators.required]),
      goal: this.fb.control(null, [Validators.required]),
      deadline: this.fb.control(null, [Validators.required]),
      tokenAddress: this.fb.control<string>('', [Validators.required]), // need to check if is valid hexstring
      description: this.fb.control<string>('', [Validators.required, Validators.maxLength(1000)])
    })
    this.creatorAddress = this.storageSvc.getAddress()
  }

  createProject() {
    let title = this.newProjectForm.get('title')?.value
    let goal = this.newProjectForm.get('goal')?.value
    let dueDate = this.newProjectForm.get('deadline')?.value
    let tokenAddress = this.newProjectForm.get('tokenAddress')?.value
    let description = this.newProjectForm.get('description')?.value

    // get 23:59 hours of that day
    let deadline = Date.parse(dueDate) + 86400000 - 1000
    this.bcSvc.createProject(goal, deadline, tokenAddress, title, description)
      .pipe(takeUntil(this.notifier$))
      .subscribe(() => {
        this.repoSvc.getLatestProject(this.creatorAddress)
          .pipe(takeUntil(this.notifier$))
          .subscribe(projectAddress => {
            console.log(projectAddress)
            let project = projectAddress as ProjectDetails
            this.router.navigateByUrl(`/project-admin/${project.projectAddress}?project=new`)
          })
      })
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
