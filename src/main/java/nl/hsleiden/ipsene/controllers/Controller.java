package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.views.View;

public interface Controller {
    public void update(DocumentSnapshot ds);
    public void registerObserver(View v);
}
