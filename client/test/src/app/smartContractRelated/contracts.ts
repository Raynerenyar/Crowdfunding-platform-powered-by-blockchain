import { CrowdfundingFunctions } from "./contract-functons/crowdfunding.contract.functions";
import { CrowdfundingFactoryFunctions } from "./contract-functons/crowdfunding.factory.contract.functions";
import { TwlvFaucetFunctions } from "./contract-functons/twlv.faucet.functions";

export class SmartContract {

    static crowdfundingFactory() {
        return new CrowdfundingFactoryFunctions('CrowdfundingFactory')
    }

    static crowdfunding() {
        return new CrowdfundingFunctions('Crowdfunding')
    }

    static twlvFaucet() {
        return new TwlvFaucetFunctions('TwlvFaucet')
    }

}