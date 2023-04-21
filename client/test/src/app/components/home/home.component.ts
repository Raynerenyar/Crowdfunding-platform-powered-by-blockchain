import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  constructor(private authSvc: AuthService) {

  }
  signIn() {

    this.authSvc.signInWithWeb3().subscribe(
      // can check for error and show appropriate message on view
      (data) => {
        console.log(data)

        // this.authSvc.
      }
    )
  }

  contribute() {
    // this.authSvc.getEncodedContributeFunctionObservable().subscribe(
    //   (data) => {
    //     console.log(data)
    //     // {encodedTransaction: '0xd7bb99ba'}
    //     // JSON.stringify(data).
    //     let a = data as { encodedTransaction: string }
    //     this.authSvc.requestApproval(a.encodedTransaction)
    //   }
    // )
    // this.authSvc.getEncodedFunctionObservable('contribute')

    let value = 100
    // if getter function, don't insert second argument
    this.authSvc.getEncodedFunctionVariableObservable('contribute', value).subscribe((resp) => {
      let a = resp as { encodedFunction: string }
      this.authSvc.sendTransaction(a.encodedFunction)
      // this.authSvc.callContract(a.encodedFunction)
      console.log(a)
      console.log(resp)
    })
  }
}
