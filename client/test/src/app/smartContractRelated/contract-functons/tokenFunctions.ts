import { ContractTxProperties } from "../../model/model";

export class TokenFunctions {

    contractName!: string
    constructor(_contractName: string) {
        this.contractName = _contractName
    }

    balanceOf(walletAddress: string) {
        let params = [walletAddress]
        return { contractName: this.contractName, functionName: 'balanceOf', parameters: params } as ContractTxProperties
    }
    approve(spenderAddress: string, amount: number) {
        let params = [spenderAddress, amount]
        return { contractName: this.contractName, functionName: 'approve', parameters: params } as ContractTxProperties
    }
}