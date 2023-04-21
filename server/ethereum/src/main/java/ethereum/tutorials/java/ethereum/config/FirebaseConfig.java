package ethereum.tutorials.java.ethereum.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @Value("${google.app.cred.filepath}")
    private String googleAppCredFile;

    @Bean
    // @Scope("singleton")
    public FirebaseApp initFirebaseApp() throws IOException {
        try {
            return FirebaseApp.getInstance();
        } catch (IllegalStateException e) {
            FileInputStream serviceAccount = new FileInputStream(googleAppCredFile);
            FirebaseOptions options = FirebaseOptions.builder()
                    // .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp app = FirebaseApp.initializeApp(options);
            // return FirebaseApp.initializeApp(options);
            serviceAccount.close();
            return app;
        }

    }
}
