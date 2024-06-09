const HDWalletProvider = require('@truffle/hdwallet-provider');
const { infuraProjectId, mnemonic } = require('./secrets.json');

module.exports = {
   networks: {
     development: {
      host: "127.0.0.1",     // Localhost (default: none)
      port: 7545,            // Standard Ethereum port (default: none)
      network_id: "*",       // Any network (default: none)
     },
  sepolia: {
    provider: () => new HDWalletProvider(mnemonic, `https://sepolia.infura.io/v3/${infuraProjectId}`),
    network_id: "11155111", // Sㅊepolia network ID
    gas: 5500000, // Gas limit
    confirmations: 2, // Number of confirmations to wait between deployments
    skipDryRun: true,
    pollingInterval : 1800000,
    disableConfirmationListener: true
  },
},
  compilers: {
    solc: {
      version: "0.8.21",      // Fetch exact version from solc-bin (default: truffle's version)
      // docker: true,        // Use "0.5.1" you've installed locally with docker (default: false)
      settings: {          // See the solidity docs for advice about optimization and evmVersion
        optimizer: {
          enabled: false,
          runs: 200
        },
        evmVersion: "byzantium"
      }
    }
  }
};
