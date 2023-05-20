import { ContractTxProperties } from "../../model/model";

export class CrowdfundingFunctions {

    contractName!: string

    constructor(_contractName: string) {
        this.contractName = _contractName
    }

    // all transactions
    contribute(amount: number) {
        let param = [amount]
        return { contractName: this.contractName, functionName: 'contribute', parameters: param } as ContractTxProperties
    }
    getRefund() {
        // let param = [projectAddress]
        return { contractName: this.contractName, functionName: 'getRefund' } as ContractTxProperties
    }
    createRequest(title: string, recipient: string, amount: number) {
        let param = [title, recipient, amount]
        console.log(param)
        return { contractName: this.contractName, functionName: 'createRequest', parameters: param } as ContractTxProperties
    }
    voteRequest(requestNum: number) {
        let param = [requestNum]
        return { contractName: this.contractName, functionName: 'voteRequest', parameters: param } as ContractTxProperties
    }
    receiveContribution(requestNum: number) {
        let param = [requestNum]
        return { contractName: this.contractName, functionName: 'receiveContribution', parameters: param } as ContractTxProperties
    }


    // view functions
    goal() { return { contractName: this.contractName, functionName: 'goal' } as ContractTxProperties }
    deadline() { return { contractName: this.contractName, functionName: 'deadline' } as ContractTxProperties }
    getBalance() { return { contractName: this.contractName, functionName: 'getBalance' } as ContractTxProperties }
    getRequestValueOfVotes(requestNum: number) {
        let param = [requestNum]
        return { contractName: this.contractName, functionName: 'getRequestValueOfVotes', parameters: param } as ContractTxProperties
    }
    raisedAmount() { return { contractName: this.contractName, functionName: 'raisedAmount' } as ContractTxProperties }
    tokenAddress() { return { contractName: this.contractName, functionName: 'tokenAddress' } as ContractTxProperties }
    contributors() { return { contractName: this.contractName, functionName: 'contributors' } as ContractTxProperties }

}
