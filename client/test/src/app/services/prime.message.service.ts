import { Injectable, OnDestroy } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Subscription, timer } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PrimeMessageService implements OnDestroy {
  isVisible!: boolean;
  timerSub$!: Subscription;
  commonProperty = {
    styleClass: 'font-size: small;', life: 3000
  }

  constructor(private primeMsgSvc: MessageService) { }
  showMsg() {
    this.isVisible = true
    this.timerSub$ = timer(5000).subscribe(t => {
      this.isVisible = false
    })
  }

  connectWalletMsg() {
    this.primeMsgSvc.add({ severity: 'info', summary: 'Wallet Connection', detail: 'Please connect your wallet.', ...this.commonProperty })
  }

  requestToInstallMetamask() {
    this.primeMsgSvc.add({ severity: 'error', summary: 'Wallet provider not detected.', detail: 'Please install Metamask.', ...this.commonProperty })
  }

  successfulWalletConnection(address: string) {
    this.primeMsgSvc.add({ severity: 'success', summary: 'Wallet connected.', detail: address, ...this.commonProperty })
  }

  successfulLogin() {
    this.primeMsgSvc.add({ severity: 'success', summary: 'Login Successful.', detail: 'You\'re in!', ...this.commonProperty })
  }

  failedToLogin(msg: string) {
    this.primeMsgSvc.add({ severity: 'error', summary: 'Failed to login', detail: msg, ...this.commonProperty })
  }

  failedToRegister(msg: string) {
    this.primeMsgSvc.add({
      severity: 'error', summary: 'Failed to register', detail: msg
      , ...this.commonProperty
    })
  }

  failedToSignMsg(msg: string) {
    this.primeMsgSvc.add({ severity: 'error', summary: 'Failed to verify signed message', detail: msg, ...this.commonProperty })
  }

  generalErrorMethod(msg: string) {
    this.primeMsgSvc.add({
      severity: 'error', summary: 'Error!', detail: msg, ...this.commonProperty
    })
  }

  detailedErrorMethod(summary: string, detail: string) {
    this.primeMsgSvc.add({
      severity: 'error', summary: summary, detail: detail, ...this.commonProperty
    })
  }

  generalSuccessMethod(msg: string) {
    this.primeMsgSvc.add({
      severity: 'success', summary: 'Success!', detail: msg, ...this.commonProperty
    })
  }

  generalInfoMethod(summary: string, msg: string) {
    this.primeMsgSvc.add({
      severity: 'info', summary: summary, detail: msg, ...this.commonProperty
    })
  }

  generalWarnMethod(summary: string, msg: string) {
    this.primeMsgSvc.add({
      severity: 'warn', summary: summary, detail: msg, ...this.commonProperty
    })
  }



  signedOut() {
    this.primeMsgSvc.add({
      severity: 'info', summary: 'You Have Signed Out.', detail: "Please Disconnect/ Lock Your Wallet As Well.", ...this.commonProperty
    })
  }

  private clearMsg() {
    this.timerSub$ = timer(5000).subscribe(t => {
      this.primeMsgSvc.clear();
    })
  }

  ngOnDestroy(): void {
    if (this.timerSub$) this.timerSub$.unsubscribe()
  }
}
