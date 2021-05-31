package nl.hsleiden.ipsene.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;

public class Firebase {

  private final Firestore store;

  protected Firebase(String privateKeyPath) throws IOException {
    FileInputStream serviceAccount = new FileInputStream(privateKeyPath);
    GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
    FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
    FirebaseApp.initializeApp(options);
    this.store = FirestoreClient.getFirestore();
  }

  protected Firestore getStore() {
    return store;
  }
}
