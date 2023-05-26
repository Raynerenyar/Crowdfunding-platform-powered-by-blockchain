import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Project, Token } from 'src/app/model/model';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { ValidatorService } from 'src/app/services/validator.service';
import { MessageService } from 'primeng/api';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { HttpClient } from '@angular/common/http';
// import {  } from "../../../../assets/misc";


interface City {
  name: string;
  code: string;
}


@Component({
  selector: 'app-new-project',
  templateUrl: './new-project.component.html',
  styleUrls: ['./new-project.component.css']
})
export class NewProjectComponent implements OnInit, OnDestroy {

  newProjectForm!: FormGroup
  charCount = 0
  charLimit = 1000
  creatorAddress!: string
  submitted = false
  notifier$ = new Subject<boolean>()
  tokens: Token[] = []
  cities!: City[]
  displayTokenAddress!: string
  @ViewChild('submitButton')
  submitButton!: ElementRef;

  constructor(
    private fb: FormBuilder,
    private bcSvc: BlockchainService,
    private router: Router,
    private repoSvc: SqlRepositoryService,
    private storageSvc: SessionStorageService,
    private validatorSvc: ValidatorService,
    private msgSvc: PrimeMessageService,
    private cdr: ChangeDetectorRef,
    private http: HttpClient,
  ) { }

  ngOnInit(): void {
    this.repoSvc.getTokens()
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (tokens: Token[]) => {
          this.tokens = tokens
          this.cdr.detectChanges()
        },
        error: (error) => {
          console.log(error)
        }
      })

    this.newProjectForm = this.fb.group({
      title: this.fb.control(null, [Validators.required]),
      goal: this.fb.control(null, [Validators.required]),
      deadline: this.fb.control(null, [Validators.required, this.validatorSvc.futureDateValidator()]), // futureDateValidator()
      tokenAddress: this.fb.control('', [Validators.required, this.validatorSvc.addressValidator()]),
      imageUrl: this.fb.control(null, [Validators.required, Validators.pattern('(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?')]),
      description: this.fb.control<string>('', [Validators.required, Validators.maxLength(this.charLimit)])
    })
    this.creatorAddress = this.storageSvc.getAddress()

    this.http.get("../../../../assets/misc/products.json")
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (data: any) => {
          let url = "https://primefaces.org/cdn/primeng/images/demo/product/"
          let index = this.getRandomInt(0, 30)
          let d = data as { data: any }
          let dataObj = d.data
          console.log(`${url}${dataObj[index].image}`)
        }
      })
  }

  createProject() {

    // token address from user selection of the dropdown or user input
    let tokenStringOrObject = this.newProjectForm.get('tokenAddress')?.value
    let tokenAddress: string = ''
    if (typeof tokenStringOrObject == 'string') {
      tokenAddress = tokenStringOrObject
    }
    if (typeof tokenStringOrObject == 'object') {
      tokenAddress = tokenStringOrObject.tokenAddress
    }
    this.submitted = true

    this.newProjectForm.get('description')?.invalid
    if (this.newProjectForm.valid) {
      console.log("creating project")
      let title = this.newProjectForm.get('title')?.value
      let goal = this.newProjectForm.get('goal')?.value
      let dueDate = this.newProjectForm.get('deadline')?.value
      let imageUrl = this.newProjectForm.get('imageUrl')?.value
      // let tokenAddress = this.newProjectForm.get('tokenAddress')?.value
      let description = this.newProjectForm.get('description')?.value

      // get 23:59 hours of that day
      let deadline = Date.parse(dueDate) + 86400000 - 1000
      this.bcSvc.createProject(goal, deadline, tokenAddress, title, description, imageUrl)
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: () => {
            this.msgSvc.generalSuccessMethod("Project has been created.")
            this.router.navigateByUrl('/project-admin')
          },
          error: () => {
            this.submitted = false
            this.msgSvc.detailedErrorMethod("Execution", "Address provided is not valid.")
          }
        })

    }
  }

  goBack() {
    this.router.navigate(['project-admin'])
  }

  onSelection(event: any) {
    if (typeof event.value == 'object') {
      this.displayTokenAddress = event.value.tokenAddress
    }
    if (typeof event.value == 'string') {
      this.displayTokenAddress = ''
    }
  }

  getRandomInt(min: number, max: number) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min) + min); // The maximum is exclusive and the minimum is inclusive
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
