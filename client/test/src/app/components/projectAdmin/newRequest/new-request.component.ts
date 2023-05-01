import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BlockchainService } from 'src/app/services/blockchain.service';

@Component({
  selector: 'app-new-request',
  templateUrl: './new-request.component.html',
  styleUrls: ['./new-request.component.css']
})
export class NewRequestComponent implements OnInit {

  newRequestForm!: FormGroup

  constructor(private fb: FormBuilder, private bcSvc: BlockchainService) { }

  ngOnInit(): void {
    this.newRequestForm = this.fb.group({
      title: this.fb.control<string>('', [Validators.required]),
      description: this.fb.control<string>('', [Validators.required]),
      recipient: this.fb.control<string>('', [Validators.required]),
      amount: this.fb.control(null, [Validators.required])
    })
  }

  createRequest() {
    let title = this.newRequestForm.get('title')?.value
    let description = this.newRequestForm.get('description')?.value
    let recipient = this.newRequestForm.get('recipient')?.value
    let amount = this.newRequestForm.get('amount')?.value
    this.bcSvc.createRequest(title, description, recipient, amount)
    // TODO: save description into mongo or sql
  }

}
