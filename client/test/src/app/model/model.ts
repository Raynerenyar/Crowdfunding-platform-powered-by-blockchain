export interface NonceResponse {
    nonce: string;
}
export interface TokenResponse {
    token: string;
}

export interface VerifyRequest {
    address: string
    recoveredAddress: string
    nonce: string
}

export interface VerifySignature {
    address: string
    signature: string

}

export const ContractFunctions = {
    contribute: "contribute",
    another: "another"
}

export interface ContractTxProperties {
    contractName: string
    functionName: string
    parameters?: any[]
}