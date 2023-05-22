package ethereum.services.ethereum;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class EtherscanService {

    @Value("${etherscan.api.url}")
    private String etherscanUrl;
    @Value("${etherscan.api.key}")
    private String etherscanApiKey;
    private static final Logger logger = LoggerFactory.getLogger(EtherscanService.class);

    public Map<String, String> getCrowdfundingSourceCode(String contractAddress) {
        String url = UriComponentsBuilder.fromUriString("https://api-sepolia.etherscan.io/api")
                .queryParam("module", "contract")
                .queryParam("action", "getsourcecode")
                .queryParam("address", contractAddress)
                .queryParam("apikey", etherscanApiKey)
                .toUriString();
        RequestEntity<Void> reqEnt = RequestEntity.get(url).build();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> respEnt = restTemplate.exchange(reqEnt, String.class);

        return parseEtherscanResponse(respEnt.getBody());
    }

    public boolean verifyContract(String contractAddress, String contractName, String... encodedParams) {

        // get source code of already verified
        Map<String, String> mapping = getCrowdfundingSourceCode(contractAddress);
        String urlString = "https://api-sepolia.etherscan.io/api";

        System.out.println(contractAddress);
        System.out.println(contractName);
        logger.info("verifying contract for {} {}", contractName, contractAddress);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("sourceCode", mapping.get("sourceCode"));
        requestBody.add("apikey", etherscanApiKey);
        requestBody.add("contractaddress", contractAddress);
        requestBody.add("contractname", contractName);
        requestBody.add("action", "verifysourcecode");
        requestBody.add("module", "contract");
        requestBody.add("codeformat", "solidity-single-file");
        requestBody.add("compilerversion", mapping.get("compilerversion"));
        requestBody.add("optimizationUsed", mapping.get("optimizationUsed"));
        requestBody.add("runs", mapping.get("runs"));

        if (encodedParams.length > 1) {
            logger.info("adding constuctor arguments");
            requestBody.add("ConstructorArguments", encodedParams[0]);
        } else {
            requestBody.add("constructorArguements", "");
        }

        // set request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(urlString, httpEntity, String.class);

        Reader reader = new StringReader(response.getBody());
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject jsonObjRespBody = jsonReader.readObject();
        logger.info("results of verifying contract {}", jsonObjRespBody);
        int result = Integer.parseInt(jsonObjRespBody.getString("status"));
        if (result == 1)
            return true;
        return false;
    }

    private Map<String, String> parseEtherscanResponse(String responseBody) {

        Reader reader = new StringReader(responseBody);
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject jsonObjRespBody = jsonReader.readObject();
        int onlyOneResult = 0;
        JsonObject jsonObj = jsonObjRespBody.getJsonArray("result")
                .get(onlyOneResult)
                .asJsonObject();
        String sourceCode = jsonObj.getString("SourceCode");

        String runs = jsonObj.getString("Runs");
        String optimisationUsed = jsonObj.getString("OptimizationUsed");
        String compilerVersion = jsonObj.getString("CompilerVersion");

        Map<String, String> mapz = new HashMap<>();
        mapz.put("sourceCode", sourceCode);
        mapz.put("compilerversion", compilerVersion);
        mapz.put("optimizationUsed", optimisationUsed);
        mapz.put("runs", runs);
        return mapz;
    }
}
