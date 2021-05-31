package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.views.View;

import java.util.ArrayList;
import java.util.List;

public class Account implements Model {

  private final String uid = "";
  private final String pwd = "";

  public Account() {}

  public boolean validateLogin(String uid, String pwd) {
    return (uid.equals(this.uid)) && (pwd.equals(this.pwd));
  }

  @Override
  public void registerObserver(View v) {

  }

  @Override
  public void unregisterObserver(View v) {

  }

  @Override
  public void notifyObservers() {

  }
}
