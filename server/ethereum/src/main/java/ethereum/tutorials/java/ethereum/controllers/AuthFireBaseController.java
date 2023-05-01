package ethereum.tutorials.java.ethereum.controllers;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.security.SignatureException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.firebase.auth.FirebaseAuthException;

import ethereum.tutorials.java.ethereum.models.NonceResponse;
import ethereum.tutorials.java.ethereum.service.ethereum.BlockchainService;
import ethereum.tutorials.java.ethereum.service.firebase.FirebaseService;
import ethereum.tutorials.java.ethereum.util.misc.Util;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Controller
// @CrossOrigin(origins = "*")
// @CrossOrigin(origins = "#{'${client.url}'}")
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
public class AuthFireBaseController {

    @Autowired
    private FirebaseService firebaseSvc;
    @Autowired
    private BlockchainService BcSvc;

    @PostMapping(path = "/get-nonce", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getNonce(@RequestBody String body) {
        System.out.println("get mapping controller >>>>>> " + body);
        JsonObject jsonObjResqBody = Util.readJson(body);
        String address = jsonObjResqBody.getString("address");
        System.out.println(address);
        try {
            String nonce = firebaseSvc.getNonce(address);
            String nonceResponse = Json.createObjectBuilder()
                    .add("nonce", nonce)
                    .build()
                    .toString();
            System.out.println("got the nonce >>>>>> " + nonce);
            return ResponseEntity.status(HttpStatus.OK).body(nonceResponse);
            // return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(path = "/get-token", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> verifySigned(@RequestBody String body) throws SignatureException {
        JsonObject json = Util.readJson(body);
        String address = json.getString("address");
        String recoveredAddress = json.getString("recoveredAddress");
        // String nonce = json.getString("nonce");
        System.out.println("address sent in >>>>> " + address);
        System.out.println("recoveredAddress sent in >>>>> " + recoveredAddress);
        try {
            Optional<String> opt = firebaseSvc.verifySignedMessage(address, recoveredAddress);
            if (opt.isPresent()) {
                String tokenResponse = Json.createObjectBuilder()
                        .add("token", opt.get())
                        .build()
                        .toString();
                return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
                // return ResponseEntity.status(HttpStatus.OK).body(null);
            }
            throw new Exception("Error encountered");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/verify-signature")
    public ResponseEntity<String> verifySignature(@RequestParam String sig, @RequestParam String nonce,
            @RequestParam String address) {
        System.out.println("verify api >>>> " + sig);
        System.out.println("verify api >>>> " + nonce);
        if (this.BcSvc.verifySignedMessage(sig, nonce, address)) {
            Optional<String> opt = firebaseSvc.getToken(address);
            System.out.println(opt.get());
            if (opt.isPresent()) {
                String tokenResponse = Json.createObjectBuilder()
                        .add("token", opt.get())
                        .build()
                        .toString();
                return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    // @PostMapping(path = "/get-function-encoded/{variableName}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<String> contractVariables(@PathVariable String variableName, @RequestBody Object[] params) {
    //     try {
    //         Optional<String> opt;
    //         if (params.length == 0) {
    //             opt = BcSvc.xxgetFunctionEncoded(variableName, contractAddress);
    //             // System.out.println("testing s>> " + encodedFunction);
    //         } else {
    //             opt = BcSvc.xxgetFunctionEncoded(variableName, contractAddress, params);
    //             // System.out.println("testing s>> " + encodedFunction);
    //         }
    //         if (opt.isPresent()) {
    //             System.out.println(opt.get());
    //             String encodedJsonObj = Json.createObjectBuilder()
    //                     .add("encodedFunction", opt.get())
    //                     .build()
    //                     .toString();
    //             return ResponseEntity.status(HttpStatus.OK).body(encodedJsonObj);
    //         }
    //     } catch (Exception e) {
    //         System.out.println(e.getMessage());
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    //     }
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // }
}
