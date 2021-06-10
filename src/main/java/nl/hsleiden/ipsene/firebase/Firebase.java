package nl.hsleiden.ipsene.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Firebase {
  public static final String CARD_FIELD_NAME = "cards";
  public static final String TEAM_FIELD_NAME = "teams";
  public static final String DOING_TURN_FIELD_NAME = "doingTurn";
  public static final String ROUND_FIELD_NAME = "round";
  public static final String TURN_START_TIME_FIELD_NAME = "turnStartTime";
  private final Firestore store;

  protected Firebase(String privateKeyPath) throws IOException {
    InputStream serviceAccount = this.getClass().getResourceAsStream(privateKeyPath);
    if(serviceAccount == null) throw new FileNotFoundException("The firebase private key could not be found in: " + privateKeyPath);
    GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
    FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
    FirebaseApp.initializeApp(options);
    this.store = FirestoreClient.getFirestore();
  }

  protected Firestore getStore() {
    return store;
  }
}
