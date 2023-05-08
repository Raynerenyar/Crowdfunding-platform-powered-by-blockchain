import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';

@Component({
  selector: 'app-new-request',
  templateUrl: './new-request.component.html',
  styleUrls: ['./new-request.component.css']
})
export class NewRequestComponent implements OnInit, OnDestroy {

  newRequestForm!: FormGroup
  projectAddress!: string
  notifier$ = new Subject<boolean>()

  constructor(private fb: FormBuilder, private bcSvc: BlockchainService, private repoSvc: SqlRepositoryService, private route: ActivatedRoute, private storageSvc: SessionStorageService, private router: Router) { }

  ngOnInit(): void {
    let walletAddress = this.storageSvc.getUser()
    console.log(walletAddress.username)
    this.newRequestForm = this.fb.group({
      title: this.fb.control<string>('', [Validators.required]),
      description: this.fb.control<string>('', [Validators.required]),
      recipient: this.fb.control<string>(walletAddress.username, [Validators.required]),
      amount: this.fb.control(null, [Validators.required])
    })
    this.repoSvc.projectAddressEvent.subscribe((address) => {
      console.log(address)
      this.projectAddress = address
    })

    this.route.paramMap.subscribe((params: ParamMap) => {
      let address = params.get('address')
      if (address) this.projectAddress = address
    })
  }

  createRequest() {
    let title = this.newRequestForm.get('title')?.value
    let description = this.newRequestForm.get('description')?.value
    let recipient = this.newRequestForm.get('recipient')?.value
    let amount = this.newRequestForm.get('amount')?.value
    console.log(this.projectAddress)
    this.bcSvc.createRequest(this.projectAddress, title, description, recipient, amount)
      .pipe(takeUntil(this.notifier$))
      .subscribe(() => this.router.navigate(['project-admin', 'current', this.projectAddress]))
    // TODO: save description into mongo or sql
  }

  ngOnDestroy(): void {

  }
}
