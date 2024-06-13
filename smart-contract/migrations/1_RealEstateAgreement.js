const test = artifacts.require("RealEstateAgreement");

module.exports = function (deployer) {
  deployer.deploy(test);
};