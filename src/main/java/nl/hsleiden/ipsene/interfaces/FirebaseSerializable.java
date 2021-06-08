package nl.hsleiden.ipsene.interfaces;

import com.google.cloud.firestore.DocumentSnapshot;

public interface FirebaseSerializable<T> {
    T serialize();
    void update(DocumentSnapshot document);
}
