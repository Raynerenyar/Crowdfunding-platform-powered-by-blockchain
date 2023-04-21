package ethereum.tutorials.java.ethereum.service.firebase;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.checkerframework.checker.units.qual.s;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ethereum.tutorials.java.ethereum.model.NonceResponse;

@Service
public class FirebaseService {
    @Autowired
    FirebaseApp firebase;

    public String getNonce(String address) throws InterruptedException, ExecutionException {
        address = address.toLowerCase();
        Optional<String> opt = checkAccountExist(address);
        if (opt.isPresent()) {
            return opt.get();
        }
        // create user and return generated nonce
        return createUser(address);
    }

    public Optional<String> verifySignedMessage(String address, String recoveredAddress)
            throws FirebaseAuthException, InterruptedException, ExecutionException {
        if (address.compareToIgnoreCase(recoveredAddress) == 0 && checkAccountExist(address).isPresent()) {
            // getFirebaseDocument(address).
            return Optional.of(FirebaseAuth.getInstance().createCustomToken(address));
        }
        return Optional.empty();
    }

    public Optional<String> getToken(String address) {
        try {
            String token = FirebaseAuth.getInstance().createCustomToken(address);
            return Optional.of(token);
        } catch (FirebaseAuthException e) {
            return Optional.empty();
        }

    }

    public Optional<String> checkAccountExist(String address) throws InterruptedException, ExecutionException {
        String nonce;
        DocumentReference docRef = getFirebaseDocument(address);
        DocumentSnapshot doc = docRef.get().get();
        if (doc.exists()) {
            nonce = doc.getString("nonce");
            return Optional.of(nonce);
        }
        return Optional.empty();
    }

    private String createUser(String address) throws InterruptedException, ExecutionException {
        String nonce = UUID.randomUUID().toString().substring(0, 8);
        NonceResponse non = new NonceResponse(nonce);
        DocumentReference docRef = getFirebaseDocument(address);
        docRef.set(non);
        return nonce;
    }

    private DocumentReference getFirebaseDocument(String address) throws InterruptedException, ExecutionException {
        address = address.toLowerCase();
        Firestore firestore = FirestoreClient.getFirestore(firebase);
        return firestore.collection("users").document(address);
    }
}
