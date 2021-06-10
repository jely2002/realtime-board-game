package nl.hsleiden.ipsene.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;

public class Firebase {
  public static final String CARD_FIELD_NAME = "cards";
  public static final String PLAYER_FIELD_NAME = "players";
  public static final String DOING_TURN_FIELD_NAME = "doingTurn";
  public static final String ROUND_FIELD_NAME = "round";
  public static final String TURN_START_TIME_FIELD_NAME = "turnStartTime";
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