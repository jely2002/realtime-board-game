package nl.hsleiden.ipsene.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import nl.hsleiden.ipsene.controllers.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirebaseService {

  private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class.getName());

  private Firestore firestore;
  private CollectionReference colRef;

  public FirebaseService(String privateKeyPath, String collection) throws IOException {
    Firebase fb = new Firebase(privateKeyPath);
    this.firestore = fb.getStore();
    this.colRef = this.firestore.collection(collection);
  }

  /**
   * Geeft een update naar de meegeleverde controller op het moment dat er een wijziging in het
   * firebase document plaatsvindt.
   *
   * @param documentId
   */
  public void listen(String documentId, final Controller controller) {

    DocumentReference docRef = this.colRef.document(documentId);

    docRef.addSnapshotListener(
        (snapshot, e) -> {
          if (e != null) {
            logger.error("listen failed", e);
            return;
          }

          if (snapshot != null && snapshot.exists()) {

            controller.update(snapshot);

            logger.debug("listener received data: {}", snapshot.getData());
          } else {
            logger.warn("listener received null data");
          }
        });
  }

  /**
   * Overschrijft een document als het als bestaat of maakt een nieuwe aan. Wees hier dus
   * voorzichtig mee.
   *
   * @param docData
   * @param documentId
   */
  public void set(String documentId, Map<String, Object> docData) {
    ApiFuture<WriteResult> future = this.colRef.document(documentId).set(docData);
    try {
      logger.debug("time to update: {}", future.get().getUpdateTime());
    } catch (InterruptedException | ExecutionException e) {
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * Verkrijgen van 1 document op basis van een documentId.
   *
   * @param documentId
   * @return
   */
  public DocumentSnapshot get(String documentId) {

    DocumentReference docRef = this.colRef.document(documentId);
    ApiFuture<DocumentSnapshot> future = docRef.get();
    DocumentSnapshot document;

    try {
      document = future.get();

      if (document.exists()) {
        return document;
      } else {
        logger.warn("document with Id {} does not exist", documentId);
      }
    } catch (InterruptedException | ExecutionException e) {
      logger.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * Verwijdert een document op basis van het documentId.
   *
   * @param documentId
   */
  public void delete(String documentId) {
    ApiFuture<WriteResult> writeResult = this.colRef.document(documentId).delete();
    // todo: give feedback oid..
  }
}
