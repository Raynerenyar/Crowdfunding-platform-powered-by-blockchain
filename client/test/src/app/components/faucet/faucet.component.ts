import { Component } from '@angular/core';
import { BlockchainService } from 'src/app/services/blockchain.service';

@Component({
  selector: 'app-faucet',
  templateUrl: './faucet.component.html',
  styleUrls: ['./faucet.component.css']
})
export class FaucetComponent {

  constructor(private bcSvc: BlockchainService) { }

  distribute() {
    this.bcSvc.distributeFromFaucet();
  }
}
