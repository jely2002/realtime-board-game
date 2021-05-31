package nl.hsleiden.ipsene.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import nl.hsleiden.ipsene.controllers.Controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseService {

    private Firestore firestore;
    private CollectionReference colRef;

    public FirebaseService(String privateKeyPath, String collection) throws IOException {
        Firebase fb = new Firebase(privateKeyPath);
        this.firestore = fb.getStore();
        this.colRef = this.firestore.collection(collection);
    }

    /**
     * Geeft een update naar de meegeleverde controller
     * op het moment dat er een wijziging in het firebase document plaatsvindt.
     * @param documentId
     */
    public void listen(String documentId, final Controller controller) {

        DocumentReference docRef = this.colRef.document(documentId);

        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                System.err.println("Listen failed: " + e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {

                controller.update(snapshot);

                System.out.println("Current data: " + snapshot.getData());
            } else {
                System.out.print("Current data: null");
            }
        });
    }

    /**
     * Overschrijft een document als het als bestaat of maakt een nieuwe aan.
     * Wees hier dus voorzichtig mee.
     * @param docData
     * @param documentId
     */
    public void set(String documentId, Map<String, Object> docData) {

        ApiFuture<WriteResult> future = this.colRef.document(documentId).set(docData);

        try {
            System.out.println("Update time : " + future.get().getUpdateTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * Verkrijgen van 1 document op basis van een documentId.
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

                System.out.println("No such document!");
            }
        } catch (InterruptedException e) {

            e.printStackTrace();
        } catch (ExecutionException e) {

            e.printStackTrace();
        }
        return null;

    }


    /**
     * Verwijdert een document op basis van het documentId.
     * @param documentId
     */
    public void delete(String documentId) {
        ApiFuture<WriteResult> writeResult = this.colRef.document(documentId).delete();
        // todo: give feedback oid..
    }
}
