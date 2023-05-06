import { CrowdfundingFunctions } from "./contract-functons/crowdfunding.contract.functions";
import { CrowdfundingFactoryFunctions } from "./contract-functons/crowdfunding.factory.contract.functions";
import { TokenFunctions } from "./contract-functons/tokenFunctions";
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

    static TWLV() {
        return new TokenFunctions('Token')
    }

}