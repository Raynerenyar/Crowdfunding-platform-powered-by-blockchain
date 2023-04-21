import { ContractTxProperties } from "../../model/model";

export class CrowdfundingFunctions {

    contractName!: string

    constructor(_contractName: string) {
        this.contractName = _contractName
    }

    // view functions
    goal() { return { contractName: this.contractName, functionName: 'goal' } as ContractTxProperties }
    deadline() { return { contractName: this.contractName, functionName: 'deadline' } as ContractTxProperties }
    getBalance() { return { contractName: this.contractName, functionName: 'getBalance' } as ContractTxProperties }
    requests() { return { contractName: this.contractName, functionName: 'requests' } as ContractTxProperties }
    raisedAmount() { return { contractName: this.contractName, functionName: 'raisedAmount' } as ContractTxProperties }
    tokenAddress() { return { contractName: this.contractName, functionName: 'tokenAddress' } as ContractTxProperties }
    contributers() { return { contractName: this.contractName, functionName: 'contributers' } as ContractTxProperties }

}
