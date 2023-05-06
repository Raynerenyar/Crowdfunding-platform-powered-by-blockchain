package ethereum.tutorials.java.ethereum.services.ethereum;

import java.io.Reader;
import java.io.StringReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public String getSourceCode(String contractAddress) {
        String url = UriComponentsBuilder.fromUriString(etherscanUrl)
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

    public boolean verifyContract(String contractAddress, String contractName, String encodedParams) {

        String sourceCode = getSourceCode(contractAddress);
        String url = UriComponentsBuilder.fromUriString(etherscanUrl)
                .queryParam("module", "contract")
                .queryParam("action", "verifysourcecode")
                .queryParam("address", contractAddress)
                .queryParam("apikey", etherscanApiKey)
                .queryParam("sourceCode", sourceCode)
                .queryParam("codeformat", "solidity-single-file")
                .queryParam("contractname", contractName)
                .queryParam("compilerversion", "0.8.19+commit.7dd6d404")
                .queryParam("optimizationUsed", 1)
                .queryParam("runs", 200)
                .queryParam("constructorArguement", encodedParams)
                .toUriString();
        RequestEntity<Void> reqEnt = RequestEntity.post(url).build();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> respEnt = restTemplate.exchange(reqEnt, String.class);

        Reader reader = new StringReader(respEnt.getBody());
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject jsonObjRespBody = jsonReader.readObject();
        int result = jsonObjRespBody.getInt("status");
        if (result == 1)
            return true;
        return false;
    }

    private String parseEtherscanResponse(String responseBody) {
        Reader reader = new StringReader(responseBody);
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject jsonObjRespBody = jsonReader.readObject();
        int onlyOneResult = 0;
        String sourceCode = jsonObjRespBody.getJsonArray("result")
                .get(onlyOneResult)
                .asJsonObject()
                .getString("Source");
        return sourceCode;
    }
}
