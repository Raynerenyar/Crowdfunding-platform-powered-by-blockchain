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

export interface ContractTxProperties {
    contractName: string
    functionName: string
    parameters?: any[]
}


export interface ProjectDetails {
    projectAddress: string
    creatorAddress: string
    title: string
    description: string
    goal: string
    deadline: Date
    raisedAmount: number
    completed: boolean
    expired: boolean
    numOfRequests: number
    acceptingToken: string
    createdDate: Date
}

export interface RequestDetails {
    requestId: number
    projectAddress: string
    title: string
    description: string
    recipientAddress: string
    amount: number
    numOfVotes: number
    completed: boolean
    valueOfVotes: number
    tokenName: string
}