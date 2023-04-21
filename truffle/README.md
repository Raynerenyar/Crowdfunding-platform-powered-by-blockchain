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

## verify using truffle-plugin-verify
`truffle run verify <contract name> --forceConstructorArgs string:00000000000000000000000000000000000000000000000000000000000003e80000000000000000000000000000000000000000000000000000000000001b58 --network dashboard --verifiers=etherscan`

`--forceConstructorArgs string:<abi encoded arguments>` convert arguments to abi encoded string [https://abi.hashex.org/](https://abi.hashex.org/)
`--network <network name, the one in truffle-config.js>`
`--verifiers=etherscan` by default truffle deploy to etherscan and sourcify

`<contract name>@a<contract address>` for targeting specific contract address

## testing with truffle-suit
run ganache first
`ganache`
`truffle test`
`truffle test <js file name in test folder>`