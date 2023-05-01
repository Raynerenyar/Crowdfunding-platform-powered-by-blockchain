import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BlockchainService } from 'src/app/services/blockchain.service';

@Component({
  selector: 'app-project-admin',
  templateUrl: './project-admin.component.html',
  styleUrls: ['./project-admin.component.css']
})
export class ProjectAdminComponent implements OnInit {

  newProjectForm!: FormGroup
  // newRequestForm!: FormGroup
  // requestNumForm!: FormGroup
  charCount!: number

  constructor(private fb: FormBuilder, private bcSvc: BlockchainService) { }

  ngOnInit(): void {
    this.newProjectForm = this.fb.group({
      goal: this.fb.control(null, [Validators.required]),
      deadline: this.fb.control(null, [Validators.required]),
      tokenAddress: this.fb.control<string>('', [Validators.required]), // need to check if is valid hexstring
      description: this.fb.control<string>('', [Validators.required, Validators.maxLength(1000)])
    })
  }

  createProject() {
    let goal = this.newProjectForm.get('goal')?.value
    let dueDate = this.newProjectForm.get('deadline')?.value
    let tokenAddress = this.newProjectForm.get('tokenAddress')?.value
    let description = this.newProjectForm.get('description')?.value

    let deadline = Date.parse(dueDate)
    this.bcSvc.createProject(goal, deadline, tokenAddress, description)
  }

  // receiveContribution() {
  //   let requestNum = this.requestNumForm.get('requestNum')?.value
  //   this.bcSvc.receiveContribution(requestNum)
  // }

  // createRequest() {
  //   let description = this.newRequestForm.get('description')?.value
  //   let recipient = this.newRequestForm.get('recipient')?.value
  //   let amount = this.newRequestForm.get('amount')?.value
  //   this.bcSvc.createRequest(description, recipient, amount)
  // }
}
