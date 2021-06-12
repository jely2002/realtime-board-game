package nl.hsleiden.ipsene.interfaces;

import com.google.cloud.firestore.DocumentSnapshot;

public interface Controller {
  void update(DocumentSnapshot ds);

  void registerObserver(View v);
}
