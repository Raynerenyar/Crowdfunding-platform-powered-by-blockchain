import { ContractTxProperties } from "src/app/model/model"

export class CrowdfundingFactoryFunctions {

    contractName!: string

    constructor(_contractName: string) {
        this.contractName = _contractName
    }

    // blockchain transactions (changes the state of the blockchain)
    createNewProject(goal: number, deadline: number, tokenAddress: string, title: string) {
        let param = [goal, deadline, tokenAddress, title]
        return { contractName: this.contractName, functionName: 'createNewProject', parameters: param } as ContractTxProperties
    }

    allowAddress(projectAddress: string) {
        let param = [projectAddress]
        return { contractName: this.contractName, functionName: 'allowAddress', parameters: param } as ContractTxProperties
    }
    disallowAddress(projectAddress: string) {
        let param = [projectAddress]
        return { contractName: this.contractName, functionName: 'disallowAddress', parameters: param } as ContractTxProperties
    }

    toggleFreeForAll() { return { contractName: this.contractName, functionName: 'toggleFreeForAll' } as ContractTxProperties }

    isContract(address: string) {
        let param = [address]
        return { contractName: this.contractName, functionName: 'isContract', parameters: param } as ContractTxProperties
    }

    isToken(address: string) {
        let param = [address]
        return { contractName: this.contractName, functionName: 'isToken', parameters: param } as ContractTxProperties
    }

}