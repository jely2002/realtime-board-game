package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.interfaces.Controller;
import nl.hsleiden.ipsene.models.Team;
import nl.hsleiden.ipsene.interfaces.View;

public class TeamController implements Controller {
  public Team[] teams;


  public TeamController() {

  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {}
}
