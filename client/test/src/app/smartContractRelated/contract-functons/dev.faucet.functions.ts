import { ContractTxProperties } from "src/app/model/model"

export class DevFaucetFunctions {

    contractName!: string

    constructor(_contractName: string) {
        this.contractName = _contractName
    }
    // transactions
    distribute() { return { contractName: this.contractName, functionName: 'distribute' } as ContractTxProperties }
    toggleFaucet() { return { contractName: this.contractName, functionName: 'toggleFaucet' } as ContractTxProperties }

    // view functions
    balance() { return { contractName: this.contractName, functionName: 'balance' } as ContractTxProperties }
    tokenAddress() { return { contractName: this.contractName, functionName: 'tokenAddress' } as ContractTxProperties }
    mintable() { return { contractName: this.contractName, functionName: 'mintable' } as ContractTxProperties }
    timestampOfLastMint(address: string) {
        let param = [address]
        return { contractName: this.contractName, functionName: 'timestampOfLastMint', parameters: param } as ContractTxProperties
    }


}