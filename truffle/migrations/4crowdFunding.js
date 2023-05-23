const Crowdfunding = artifacts.require("Crowdfunding");
const Token = artifacts.require("Token");
const Faucet = artifacts.require("DevFaucet");
const Factory = artifacts.require("CrowdfundingFactory");
const BN = require("bn.js");

module.exports = async function (deployer, network, accounts) {
  let mintAmount = 1000_000;
  let decimals = new BN(10).pow(new BN(18));
  let mintAmountBN = new BN(mintAmount).mul(decimals);

  if (network === "development") {
    token = await deployer.deploy(Token, { from: accounts[0] });
    await deployer.deploy(Factory, { from: accounts[0] });

    let tokenInstance = await Token.deployed();
    let faucet = await deployer.deploy(Faucet, tokenInstance.address, 90000, {
      from: accounts[0],
    });
    await tokenInstance.mint(faucet.address, mintAmountBN.toString(), {
      from: accounts[0],
    });
  }
  // deploying live
  else {
    token = await deployer.deploy(Token);
    await deployer.deploy(Factory);

    let tokenInstance = await Token.deployed();
    await deployer.deploy(Faucet, tokenInstance.address, 90000);
    let faucetInstance = await Faucet.deployed();
    await tokenInstance.mint(faucetInstance.address, mintAmountBN.toString());
  }
};
