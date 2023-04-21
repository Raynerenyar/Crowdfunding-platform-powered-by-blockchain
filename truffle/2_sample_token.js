const SampleToken = artifacts.require("SampleToken");

module.exports = function (deployer) {
  deployer.deploy(SampleToken, "Sample Token", "TOK", (100e18).toString());
};
