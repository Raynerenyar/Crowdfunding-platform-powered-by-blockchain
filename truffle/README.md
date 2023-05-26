# Truffle initialisation
1. `truffle init`
2. `npm init -y`
Install sol imports
3. `npm install --save <imports>`
Prefix filenames with number
4. create deploy js files
5. `npm install @truffle/hdwallet-provider`

# Local test contracts
start development blockchain with `truffle development`. Once inside then use `migrate`

# Local deploy on ganache
Start `ganache`. Start dashboard before deploying contracts `truffle dashboard`. Import wallet private key to metamask. Deploy to ganache network through dashboard `truffle migrate --network dashboard`

# Live deploy contracts
Start dashboard before deploying contracts `truffle dashboard`.Connect wallet on dashboard to the network for deployment

Deploy to the selected network through dashboard `truffle migrate --network dashboard --optimize`

## To compile .sol
`truffle compile`

## Start ganache
`ganache`
these are test ganache accounts not meant for mainnet
```
ganache --account="0xe2904547b5dc392af1862d4bda879e91c74ccfccbdbbaa5cf17bd4f9c1f5d72b,1000000000000000000000" --account="0x9b8510c66ae8b9f1f58e49255a8679aa6966569e0b01ce0b9af1ac153483d780,1000000000000000000000" --account="0x653dc458fa018050b101293c363891b9d334441878d05832dd0f20a0f62b22cf,1000000000000000000000" --account="0x7380a969d9c7a9fb3762574117ada76da6a869e6eb34154b6c95c447d6082dc7,1000000000000000000000" --account="0xb31cab9def1086d47f9b03bb528e99e21f54bb8b0ecc673c977a38b08c76a465,1000000000000000000000" --account="0xcf0e4258cd4381a466a8060ee49f9a54fec19ea53dcfeddeae09050e2699c599,1000000000000000000000"
```

## verify using truffle-plugin-verify
`truffle run verify <contract name> --forceConstructorArgs string:00000000000000000000000000000000000000000000000000000000000003e80000000000000000000000000000000000000000000000000000000000001b58 --network dashboard --verifiers=etherscan`

`--forceConstructorArgs string:<abi encoded arguments>` convert arguments to abi encoded string [https://abi.hashex.org/](https://abi.hashex.org/)
`--network <network name, the one in truffle-config.js>`
`--verifiers=etherscan` by default truffle deploy to etherscan and sourcify

`<contract name>@<contract address>` for targeting specific contract address

## testing with truffle-suit
run ganache first
`ganache`
`truffle test`
`truffle test <js file name in test folder>`