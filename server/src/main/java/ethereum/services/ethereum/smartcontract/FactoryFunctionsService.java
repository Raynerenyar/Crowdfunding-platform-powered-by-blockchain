package ethereum.services.ethereum.smartcontract;

import java.math.BigInteger;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ethereum.javaethereum.wrapper.CrowdfundingFactory;
import ethereum.models.smartcontract.Project;
import ethereum.services.ethereum.LoadContractService;

import ethereum.util.misc.Util;

@Service
public class FactoryFunctionsService {

    @Autowired
    private LoadContractService lcSvc;
    @Autowired
    private TokenFunctionsService tokenFuncSvc;
    @Value("${crowdfunding.factory.contract.address}")
    private String crowdfundingFactoryContractAddress;
    private static final Logger logger = LoggerFactory.getLogger(FactoryFunctionsService.class);

    public Optional<String> createNewProject(Project project) {
        try {
            CrowdfundingFactory crowdFactory = lcSvc
                    .loadCrowdfundingFactoryContract(crowdfundingFactoryContractAddress);
            // get decimals of token.
            int decimals = tokenFuncSvc.getTokenDecimals(project.getTokenAddress());
            String functionAbi = crowdFactory.createNewProject(
                    Util.convertToBaseUnit(project.getGoal(), decimals), // convert goal to biginteger
                    BigInteger.valueOf(project.getDeadline()), // convert to biginteger
                    project.getTokenAddress(),
                    project.getTitle())
                    .encodeFunctionCall();
            return Optional.of(functionAbi);
        } catch (Exception e) {
            logger.error("creating new project", e);
            return Optional.empty();
        }
    }
}