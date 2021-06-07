package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.models.Deck;
import nl.hsleiden.ipsene.models.Team;
import nl.hsleiden.ipsene.models.TeamType;
import nl.hsleiden.ipsene.views.View;

public class TeamController implements Controller {
  public Team[] teams;


  public TeamController() {

  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {}
}
