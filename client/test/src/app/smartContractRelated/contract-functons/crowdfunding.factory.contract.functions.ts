import { ContractTxProperties } from "src/app/model/model"

export class CrowdfundingFactoryFunctions {

    contractName!: string

    constructor(_contractName: string) {
        this.contractName = _contractName
    }

    // blockchain transactions (changes the state of the blockchain)
    createNewProject(goal: number, timeAhead: number, tokenAddress: string, description: string) {
        let param = [goal, timeAhead, tokenAddress, description]
        return { contractName: this.contractName, functionName: 'createNewProject', parameters: param } as ContractTxProperties
    }
    contributeToProject(projectAddress: string, amount: number) {
        let param = [projectAddress, amount]
        return { contractName: this.contractName, functionName: 'contributeToProject', parameters: param } as ContractTxProperties
    }
    getRefundFromProject(projectAddress: string) {
        let param = [projectAddress]
        return { contractName: this.contractName, functionName: 'getRefundFromProject', parameters: param } as ContractTxProperties
    }
    createRequestForProject(projectAddress: string, description: string, recipient: string, amount: number) {
        let param = [projectAddress, description, recipient, amount]
        return { contractName: this.contractName, functionName: 'createRequestForProject', parameters: param } as ContractTxProperties
    }
    voteRequestForProject(projectAddress: string, requestNum: number) {
        let param = [projectAddress, requestNum]
        return { contractName: this.contractName, functionName: 'voteRequestForProject', parameters: param } as ContractTxProperties
    }
    receiveContributionFromProject(projectAddress: string, requestNum: number) {
        let param = [projectAddress, requestNum]
        return { contractName: this.contractName, functionName: 'receiveContributionFromProject', parameters: param } as ContractTxProperties
    }
    allowAddress(projectAddress: string) {
        let param = [projectAddress]
        return { contractName: this.contractName, functionName: 'allowAddress', parameters: param } as ContractTxProperties
    }
    disallowAddress(projectAddress: string) {
        let param = [projectAddress]
        return { contractName: this.contractName, functionName: 'disallowAddress', parameters: param } as ContractTxProperties
    }

    /*
        view functions of the crowdfunding factory r    
     */
    getRequestOfProject(projectAddress: string, requestNo: number) {
        let param = [projectAddress, requestNo]
        return { contractName: this.contractName, functionName: 'getRequestOfProject', parameters: param } as ContractTxProperties
    }
    getNumOfRequestOfProject(projectAddress: string) {
        let param = [projectAddress]
        return { contractName: this.contractName, functionName: 'getNumOfRequestOfProject', parameters: param } as ContractTxProperties
    }
    getdescriptionOfProject(projectAddress: string) {
        let param = [projectAddress]
        return { contractName: this.contractName, functionName: 'getDescriptionOfProject', parameters: param } as ContractTxProperties
    }
}