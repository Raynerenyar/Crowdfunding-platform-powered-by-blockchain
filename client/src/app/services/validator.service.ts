import { Injectable } from '@angular/core';
import { ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { BlockchainService } from './blockchain.service';
import { Subject, firstValueFrom, takeUntil } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ValidatorService {
  notifier$ = new Subject<boolean>()

  constructor(private bcSvc: BlockchainService) { }

  nonWhitespace(): ValidatorFn {
    return (ctrl: AbstractControl): ValidationErrors | null => {
      const regex = new RegExp(/\s+/g)
      let result = ctrl.value.matchAll(regex).next().value
      if (result === undefined) {
        // if passed
        return null
      }
      // if failed
      return { nonWhitespace: "Trailing whitespaces not allowed" } as ValidationErrors
    }
  }

  // custom validator
  noNums(): ValidatorFn {
    return (ctrl: AbstractControl): ValidationErrors | null => {
      const regex = new RegExp(/[0-9]+/g)
      let result = ctrl.value.matchAll(regex).next().value
      if (result === undefined) {
        return null
      }
      return { noNums: "No numbers allowed" } as ValidationErrors
    }
  }

  // custom validator
  futureDateValidator(): ValidatorFn {
    return (ctrl: AbstractControl): ValidationErrors | null => {
      let today = new Date()
      let deadline = new Date(ctrl.value)
      if (deadline <= today) {
        return { pastDate: "Date is in the past" } as ValidationErrors
      }
      return null
    }
  }
  /** checks if address is actually an address.
  *   Does not check if address is a wallet or contract.
  *   the smart contract will check for theses.
  *   @Returns null if no error, otherwise ValidationErrors object
   */
  addressValidator(): ValidatorFn {
    return (ctrl: AbstractControl): ValidationErrors | null => {
      if (typeof ctrl.value == 'string') {
        if (this.bcSvc.isAddress(ctrl.value))
          return null
      }
      if (typeof ctrl.value == 'object') {
        let tokenAddress = ctrl.value.tokenAddress
        if (this.bcSvc.isAddress(tokenAddress))
          return null
      }
      return { address: "Address is invalid" } as ValidationErrors
    }
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
