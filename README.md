# Crowdfunding-platform-powered-by-blockchain
Welcome to an exciting crowdfunding platform powered by Ethereum blockchain technology. Here, a vibrant community of creators and backers come together to support innovative projects and turn dreams into reality.

This project was developed on the Sepolia blockchain which is a an Ethereum testnet. It enables the distribution and collection of funds to be decentralised. In the event of the web application going down, the collection of the contributed funds can be accessed through the verified smart contract. Being a verified smart contract allows anyone to see the source code[^1] of the contract as well as allows easy access to the methods[^2][^3] on a blockchain explorer such as [Sepolia Etherscan](https://sepolia.etherscan.io). Otherwise there are complicated ways to call methods of an unverified project.

# Technologies used
|Technology|Image|
|---|---|
|Solidity|<img src="https://docs.soliditylang.org/en/v0.8.17/_images/logo.svg" width="100">|
|web3js|<img src="https://raw.githubusercontent.com/Raynerenyar/Crowdfunding-platform-powered-by-blockchain/b2c477f8492bfdec2bfe19d238195dcbfa161914/client/src/assets/icons/web3js.svg" width="100">|
|web3j|<img src="https://avatars.githubusercontent.com/u/22208471?s=200&v=4" width="100">|
|Java|<img src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" width="100">|
|Maven|<img src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png" width="100">|
|Spring|<img src="https://user-images.githubusercontent.com/25181517/117201470-f6d56780-adec-11eb-8f7c-e70e376cfd07.png" width="100">|
|Spring Boot|<img src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" width="100">|
|Mysql|<img src="https://user-images.githubusercontent.com/25181517/183896128-ec99105a-ec1a-4d85-b08b-1aa1620b2046.png" width="100">|
|PostgreSQL|<img src="https://raw.githubusercontent.com/bablubambal/All_logo_and_pictures/7c0ac2ceb9f9d24992ec393d11fa7337d2f92466/databases/postgresql.svg" width="100">|
|Mongo|<img src="https://user-images.githubusercontent.com/25181517/182884177-d48a8579-2cd0-447a-b9a6-ffc7cb02560e.png" width="100">|
|Html|<img src="https://user-images.githubusercontent.com/25181517/192158954-f88b5814-d510-4564-b285-dff7d6400dad.png" width="100">|
|Css|<img src="https://user-images.githubusercontent.com/25181517/183898674-75a4a1b1-f960-4ea9-abcb-637170a00a75.png" width="100">|
|Angular|<img src="https://user-images.githubusercontent.com/25181517/183890595-779a7e64-3f43-4634-bad2-eceef4e80268.png" width="100">|
|Primeng|<img src="https://i0.wp.com/www.primefaces.org/wp-content/uploads/2018/05/primeng-logo.png?ssl=1" width="100">|
|Javascript|<img src="https://user-images.githubusercontent.com/25181517/117447155-6a868a00-af3d-11eb-9cfe-245df15c9f3f.png" width="100">|
|Typescript|<img src="https://user-images.githubusercontent.com/25181517/183890598-19a0ac2d-e88a-4005-a8df-1ee36782fde1.png" width="100">|
|Docker|<img src="https://user-images.githubusercontent.com/25181517/117207330-263ba280-adf4-11eb-9b97-0ac5b40bc3be.png" width="100">|

# Notes on use of relational database such as MySQL and PostgreSQL
Although the details of each new project and each new request can be stored in the smart contract as state variables, the use of these relational databases is for the practice of CRUD. Additionally, to avoid excessive gas cost to the project creator, the description of a project/request is not stored in the blockchain.

# Smart contract source codes
- [Crowdfunding Factory](https://sepolia.etherscan.io/address/0x88AF4fE9DB3C53D5f1CC3d5128063D36909592e9#code)
- [Example Crowdfunding, created and verified on every new project](https://sepolia.etherscan.io/address/0x768596e667842cef27d2828c9b02f37cbc61b537#code)
- [Dev token faucet](https://sepolia.etherscan.io/address/0x79d9f9AEF4E4808Db4c16b9Fffd4849063Ab8fF9#code)
- [Dev token](https://sepolia.etherscan.io/token/0x24ddd7c47d5ea7181f4d3bd67fb1d361c49020c6#code)

[^1]: [Crowdfunding smart contract source code](https://sepolia.etherscan.io/address/0x88AF4fE9DB3C53D5f1CC3d5128063D36909592e9#code)
[^2]: [Crowdfunding Smart contract read methods](https://sepolia.etherscan.io/address/0x88AF4fE9DB3C53D5f1CC3d5128063D36909592e9#readContract)
[^3]: [Crowdfunding Smart contract write methods](https://sepolia.etherscan.io/address/0x88AF4fE9DB3C53D5f1CC3d5128063D36909592e9#writeContract)
