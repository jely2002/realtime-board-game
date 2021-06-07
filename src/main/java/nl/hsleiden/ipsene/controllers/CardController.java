package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.*;
import nl.hsleiden.ipsene.models.Deck;
import nl.hsleiden.ipsene.views.View;

public class CardController implements Controller {

  static Deck generateDeck(int amountOfPlayers) {
    return new Deck(amountOfPlayers);
  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {}
}
