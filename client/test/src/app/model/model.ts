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
    tokenName: string
    tokenSymbol: string
    createdDate: Date
}

export interface RequestDetails {
    requestId: number
    requestNo: number
    projectAddress: string
    title: string
    description: string
    recipientAddress: string
    amount: number
    numOfVotes: number
    completed: boolean
}

export interface Announcement {
    projectAddress: string
    creatorAddress: string
    body: string
    datetimePosted: Date
    datetimeEdited?: Date
}

export interface NewComment {
    projectAddress: string
    posterAddress: string
    body: string
    datetimePosted: Date
}