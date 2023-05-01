const Crowdfunding = artifacts.require("Crowdfunding");
const TWLV = artifacts.require("TWLV");
const Faucet = artifacts.require("twlvFaucet");
const Factory = artifacts.require("CrowdfundingFactory");

module.exports = async function (deployer, network, accounts) {
  if (network === "development") {
    twlv = await deployer.deploy(TWLV, { from: accounts[0] });
    await deployer.deploy(Factory, { from: accounts[0] });

    let twlvInstance = await TWLV.deployed();
    let faucet = await deployer.deploy(Faucet, twlvInstance.address, 90000, {
      from: accounts[0],
    });
    await twlvInstance.mint(faucet.address, 1000000, { from: accounts[0] });
  }
  // deploying live
  else {
    twlv = await deployer.deploy(TWLV);
    await deployer.deploy(Factory);

    let twlvInstance = await TWLV.deployed();
    await deployer.deploy(Faucet, twlvInstance.address, 90000);
    let faucetInstance = await Faucet.deployed();
    await twlvInstance.mint(faucetInstance.address, 1000000);
  }
};
