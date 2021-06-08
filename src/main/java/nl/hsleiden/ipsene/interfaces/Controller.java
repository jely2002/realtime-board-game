package nl.hsleiden.ipsene.interfaces;

import com.google.cloud.firestore.DocumentSnapshot;

public interface Controller {
  public void update(DocumentSnapshot ds);

  public void registerObserver(View v);
}
