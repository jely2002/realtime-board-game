package nl.hsleiden.ipsene.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import nl.hsleiden.ipsene.exceptions.GameNotFoundException;
import nl.hsleiden.ipsene.interfaces.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirebaseService {

  private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class.getName());

  private final CollectionReference colRef;

  public FirebaseService(String privateKeyPath, String collection) throws IOException {
    Firebase fb = new Firebase(privateKeyPath);
    Firestore firestore = fb.getStore();
    this.colRef = firestore.collection(collection);
  }

  /**
   * Attaches a snapshot listener to a controller instance
   * @param documentId the document that the listener attaches to
   * @param controller the controller that listens to events
   */
  public ListenerRegistration listen(String documentId, final Controller controller) {

    DocumentReference docRef = this.colRef.document(documentId);

    return docRef.addSnapshotListener(
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

  public void removeListener(ListenerRegistration registration) {
    registration.remove();
  }

  /**
   * Overwrites the specified document with the new data
   * @param docData the data that must be written to firebase
   * @param documentId the target document id
   */
  public void set(String documentId, Map<String, Object> docData)
      throws ExecutionException, InterruptedException {
    ApiFuture<WriteResult> future = this.colRef.document(documentId).set(docData);
    logger.debug("time to update: {}", future.get().getUpdateTime());
  }

  /**
   * Gets a document snap shot from the specified documentId
   * @param documentId the document to get the data from
   * @return document snapshot containing the data
   */
  public DocumentSnapshot get(String documentId) throws ExecutionException, InterruptedException, GameNotFoundException {

    DocumentReference docRef = this.colRef.document(documentId);
    ApiFuture<DocumentSnapshot> future = docRef.get();
    DocumentSnapshot document;

    document = future.get();

    if (document.exists()) {
      return document;
    }
    logger.warn("document with Id {} does not exist", documentId);
    throw new GameNotFoundException("A game with documentId " + documentId + "does not exist.");
  }

  /**
   * Deletes a document
   * @param documentId the id of the document to delete
   */
  public void delete(String documentId) {
    ApiFuture<WriteResult> writeResult = this.colRef.document(documentId).delete();
  }
}
