package ethereum.services.auth;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ethereum.models.NonceResponse;

@Service
public class EthAuthService {
    // @Autowired
    // FirebaseApp firebase;

    public String getNonce(String address) throws InterruptedException, ExecutionException {
        address = address.toLowerCase();
        // Optional<String> opt = checkAccountExist(address);
        // if (opt.isPresent()) {
        //     return opt.get();
        // }
        // create user and return generated nonce
        return generateNonce(address);
    }

    // public Optional<String> verifySignedMessage(String address, String recoveredAddress)
    //         throws FirebaseAuthException, InterruptedException, ExecutionException {
    //     if (address.compareToIgnoreCase(recoveredAddress) == 0 && checkAccountExist(address).isPresent()) {
    //         // getFirebaseDocument(address).
    //         return Optional.of(FirebaseAuth.getInstance().createCustomToken(address));
    //     }
    //     return Optional.empty();
    // }

    // public Optional<String> getToken(String address) {
    //     try {
    //         String token = FirebaseAuth.getInstance().createCustomToken(address);
    //         return Optional.of(token);
    //     } catch (FirebaseAuthException e) {
    //         return Optional.empty();
    //     }

    // }

    // public Optional<String> checkAccountExist(String address) throws InterruptedException, ExecutionException {
    //     String nonce;
    //     DocumentReference docRef = getFirebaseDocument(address);
    //     DocumentSnapshot doc = docRef.get().get();
    //     if (doc.exists()) {
    //         // nonce = doc.getString("nonce");
    //         nonce = UUID.randomUUID().toString().substring(0, 8);
    //         return Optional.of(nonce);
    //     }
    //     return Optional.empty();
    // }

    private String generateNonce(String address) throws InterruptedException, ExecutionException {
        String nonce = UUID.randomUUID().toString().substring(0, 8);
        // NonceResponse non = new NonceResponse(nonce);
        // DocumentReference docRef = getFirebaseDocument(address);
        // docRef.set(non);
        return nonce;
    }

    // private DocumentReference getFirebaseDocument(String address) throws InterruptedException, ExecutionException {
    //     address = address.toLowerCase();
    //     Firestore firestore = FirestoreClient.getFirestore(firebase);
    //     return firestore.collection("users").document(address);
    // }
}
