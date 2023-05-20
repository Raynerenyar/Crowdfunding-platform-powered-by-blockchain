/* 
*   The abi on the client side is exposed to the browser and the user. it's fine if contract is public.
*   if contract is private it is not ideal to have the abi on the client side.
*/


enum StateMutabilityType {
    Pure = "pure",
    View = "view",
    NonPayable = "nonpayable",
    Payable = "payable",
}
enum AbiType {
    Function = "function",
    Constructor = "constructor",
    Event = "event",
    Fallback = "fallback",
}

export const tokenFunctionsAbi = {
    "balanceOf": {
        "inputs": [
            {
                "internalType": "address",
                "name": "account",
                "type": "address"
            }
        ],
        "name": "balanceOf",
        "outputs": [
            {
                "internalType": "uint256",
                "name": "",
                "type": "uint256"
            }
        ],
        "stateMutability": StateMutabilityType.View,
        "type": AbiType.Function,
        "constant": true
    },
    "approve": {
        "inputs": [
            {
                "internalType": "address",
                "name": "spender",
                "type": "address"
            },
            {
                "internalType": "uint256",
                "name": "amount",
                "type": "uint256"
            }
        ],
        "name": "approve",
        "outputs": [
            {
                "internalType": "bool",
                "name": "",
                "type": "bool"
            }
        ],
        "stateMutability": StateMutabilityType.NonPayable,
        "type": AbiType.Function
    },
    "decimals": {
        "inputs": [],
        "name": "decimals",
        "outputs": [
            {
                "internalType": "uint8",
                "name": "",
                "type": "uint8"
            }
        ],
        "stateMutability": StateMutabilityType.View,
        "type": AbiType.Function,
        "constant": true
    },
    "symbol": {
        "inputs": [],
        "name": "symbol",
        "outputs": [
            {
                "internalType": "string",
                "name": "",
                "type": "string"
            }
        ],
        "stateMutability": StateMutabilityType.View,
        "type": AbiType.Function,
        "constant": true
    },
}

export const factoryFunctionsAbi = {
    "isContract": {
        "inputs": [
            {
                "internalType": "address",
                "name": "_address",
                "type": "address"
            }
        ],
        "name": "isContract",
        "outputs": [
            {
                "internalType": "bool",
                "name": "",
                "type": "bool"
            }
        ],
        "stateMutability": "view",
        "type": "function",
        "constant": true
    },
    "isToken": {
        "inputs": [
            {
                "internalType": "address",
                "name": "_address",
                "type": "address"
            }
        ],
        "name": "isToken",
        "outputs": [
            {
                "internalType": "bool",
                "name": "",
                "type": "bool"
            }
        ],
        "stateMutability": "view",
        "type": "function",
        "constant": true
    }
}

export const crowdfundingFunctionsAbi = {

}

export function getTransactionObject(toAddress: string, data: string, fromAddress?: string) {
    if (fromAddress)
        return {
            to: toAddress,
            from: fromAddress,
            data: data
        }
    return {
        to: toAddress,
        data: data
    }
}