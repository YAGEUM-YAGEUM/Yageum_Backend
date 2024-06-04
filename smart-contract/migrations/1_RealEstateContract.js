const test = artifacts.require("RealEstateContract");

module.exports = function (deployer) {
  deployer.deploy(test);
};