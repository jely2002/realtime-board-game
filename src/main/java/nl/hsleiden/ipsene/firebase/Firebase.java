package nl.hsleiden.ipsene.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import nl.hsleiden.ipsene.views.ViewHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Firebase {
  public static final String CARD_FIELD_NAME = "cards";
  public static final String PLAYER_FIELD_NAME = "players";
  public static final String DOING_TURN_FIELD_NAME = "doingTurn";
  public static final String ROUND_FIELD_NAME = "round";
  public static final String TURN_START_TIME_FIELD_NAME = "turnStartTime";
  private final Firestore store;

  protected Firebase(String privateKeyPath) throws IOException {
    InputStream serviceAccount = ViewHelper.class.getResourceAsStream(privateKeyPath);
    if (serviceAccount == null)
      throw new FileNotFoundException("Private key file could not be found.");
    GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
    FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
    FirebaseApp.initializeApp(options);
    this.store = FirestoreClient.getFirestore();
  }

  protected Firestore getStore() {
    return store;
  }
}
