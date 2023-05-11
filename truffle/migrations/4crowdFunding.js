const Crowdfunding = artifacts.require("Crowdfunding");
const Token = artifacts.require("Token");
const Faucet = artifacts.require("DevFaucet");
const Factory = artifacts.require("CrowdfundingFactory");

module.exports = async function (deployer, network, accounts) {
  if (network === "development") {
    token = await deployer.deploy(Token, { from: accounts[0] });
    await deployer.deploy(Factory, { from: accounts[0] });

    let tokenInstance = await Token.deployed();
    let faucet = await deployer.deploy(Faucet, tokenInstance.address, 90000, {
      from: accounts[0],
    });
    await tokenInstance.mint(faucet.address, 1000000, { from: accounts[0] });
  }
  // deploying live
  else {
    token = await deployer.deploy(Token);
    await deployer.deploy(Factory);

    let tokenInstance = await Token.deployed();
    await deployer.deploy(Faucet, tokenInstance.address, 90000);
    let faucetInstance = await Faucet.deployed();
    await tokenInstance.mint(faucetInstance.address, 1000000);
  }
};
