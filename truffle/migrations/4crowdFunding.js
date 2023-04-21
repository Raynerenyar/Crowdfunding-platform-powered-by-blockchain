const Crowdfunding = artifacts.require("Crowdfunding");
const TWLV = artifacts.require("TWLV");
const Faucet = artifacts.require("twlvFaucet");
const Factory = artifacts.require("CrowdfundingFactory");

module.exports = async function (deployer, network, accounts) {
  await deployer.deploy(TWLV, { from: accounts[0] });
  await deployer.deploy(Factory, { from: accounts[0] });
  let twlvInstance = await TWLV.deployed();

  // if (network === ("test" || "develop" || "ganache" || "development")) {
  if (network === "development") {
    await deployer.deploy(Faucet, twlvInstance.address, 90000, {
      from: accounts[0],
    });
  }

  // await deployer.deploy(Crowdfunding, 500, 30, twlvInstance.address, {
  //   from: accounts[9],
  // });

  // } else {
  //   deployer.deploy(Crowdfunding, 10000, 7000, twlvInstance.address);
  // }
};
