package ethereum.security.services;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

@Service
public class NonceService {

    public String getNonce(String address) throws InterruptedException, ExecutionException {
        address = address.toLowerCase();
        return generateNonce(address);
    }

    private String generateNonce(String address) throws InterruptedException, ExecutionException {
        String nonce = UUID.randomUUID().toString().substring(0, 8);
        return nonce;
    }

}
