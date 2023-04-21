import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BlockchainService } from 'src/app/services/blockchain.service';

@Component({
  selector: 'app-project-owner',
  templateUrl: './project-owner.component.html',
  styleUrls: ['./project-owner.component.css']
})
export class ProjectOwnerComponent implements OnInit {

  newProjectForm!: FormGroup
  newRequestForm!: FormGroup
  requestNumForm!: FormGroup

  constructor(private fb: FormBuilder, private bcSvc: BlockchainService) { }

  ngOnInit(): void {
    this.newProjectForm = this.fb.group({
      goal: this.fb.control<number>(0, [Validators.required]),
      deadline: this.fb.control<number>(0, [Validators.required]),
      tokenAddress: this.fb.control<string>('', [Validators.required]),
      description: this.fb.control<string>('', [Validators.required])
    })
    this.newRequestForm = this.fb.group({
      description: this.fb.control<string>('', [Validators.required]),
      recipient: this.fb.control<string>('', [Validators.required]),
      amount: this.fb.control<number>(0, [Validators.required])
    })
    this.requestNumForm = this.fb.group({
      requestNum: this.fb.control<number>(0, [Validators.required])
    })
  }

  createProject() {
    let goal = this.newProjectForm.get('goal')?.value
    let deadline = this.newProjectForm.get('deadline')?.value
    let tokenAddress = this.newProjectForm.get('tokenAddress')?.value
    let description = this.newProjectForm.get('description')?.value
    let timeNow = Date.now()
    console.log(timeNow)
    let o = Date.parse(deadline)
    console.log(o)
    let timeAhead = o - timeNow
    console.log(timeAhead)
    this.bcSvc.createProject(goal, timeAhead, tokenAddress, description)
  }

  receiveContribution() {
    let requestNum = this.requestNumForm.get('requestNum')?.value
    this.bcSvc.receiveContribution(requestNum)
  }

  createRequest() {
    let description = this.newRequestForm.get('description')?.value
    let recipient = this.newRequestForm.get('recipient')?.value
    let amount = this.newRequestForm.get('amount')?.value
    this.bcSvc.createRequest(description, recipient, amount)
  }
}
