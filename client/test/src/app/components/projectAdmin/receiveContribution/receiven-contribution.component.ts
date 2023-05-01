import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { BlockchainService } from 'src/app/services/blockchain.service';

@Component({
  selector: 'app-receiven-contribution',
  templateUrl: './receiven-contribution.component.html',
  styleUrls: ['./receiven-contribution.component.css']
})
export class ReceivenContributionComponent implements OnInit {
  requestNumForm!: FormGroup

  constructor(private fb: FormBuilder, private bcSvc: BlockchainService) { }

  ngOnInit(): void {
    this.requestNumForm = this.fb.group({
      requestNum: this.fb.control(null, [Validators.required])
    })
  }

  receiveContribution() {
    let requestNum = this.requestNumForm.get('requestNum')?.value
    this.bcSvc.receiveContribution(requestNum)
  }

}
