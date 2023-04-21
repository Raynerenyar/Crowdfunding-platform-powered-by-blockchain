import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BlockchainService } from 'src/app/services/blockchain.service';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {

  existingProject!: FormGroup
  voteRequestForm!: FormGroup

  constructor(private fb: FormBuilder, private bcSvc: BlockchainService) { }

  ngOnInit(): void {
    this.existingProject = this.fb.group({
      contributeAmount: this.fb.control<number>(100, [Validators.required])
    })
    this.voteRequestForm = this.fb.group({
      voteRequest: this.fb.control<number>(0, [Validators.required]),
    })
  }

  voteRequest() {
    let requestNum: number = this.voteRequestForm.get('voteRequest')?.value
    this.bcSvc.voteRequest(requestNum)
  }

  contribute() {
    let amount = this.existingProject.get('contributeAmount')?.value
    this.bcSvc.contribute(amount)
  }

  refund() {
    this.bcSvc.refund()
  }


}
