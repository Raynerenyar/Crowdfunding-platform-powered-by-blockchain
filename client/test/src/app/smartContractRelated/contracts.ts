import { CrowdfundingFunctions } from "./contract-functons/crowdfunding.contract.functions";
import { CrowdfundingFactoryFunctions } from "./contract-functons/crowdfunding.factory.contract.functions";
import { TokenFunctions } from "./contract-functons/tokenFunctions";
import { DevFaucetFunctions } from "./contract-functons/dev.faucet.functions";

export class SmartContract {

    static crowdfundingFactory() {
        return new CrowdfundingFactoryFunctions('CrowdfundingFactory')
    }

    static crowdfunding() {
        return new CrowdfundingFunctions('Crowdfunding')
    }

    static DevFaucet() {
        return new DevFaucetFunctions('DevFaucet')
    }

    static Token() {
        return new TokenFunctions('Token')
    }

}