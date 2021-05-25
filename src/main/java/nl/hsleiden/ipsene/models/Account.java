package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.observers.AccountObservable;
import nl.hsleiden.ipsene.observers.AccountObserver;

import java.util.ArrayList;
import java.util.List;

public class Account implements AccountObservable {

  private final String uid = "";
  private final String pwd = "";

  public Account() {}

  public boolean validateLogin(String uid, String pwd) {
    return (uid.equals(this.uid)) && (pwd.equals(this.pwd));
  }

  private List<AccountObserver> observers = new ArrayList<AccountObserver>();

  // Add an observer to the list
  public void register(AccountObserver ao){
    observers.add(ao);
  }
  // Signal all observers that something has changed.
  // Also send <<this>> object to the observers.
  public void notifyAllObservers(){
    for (AccountObserver ao : observers) {
      ao.update((AccountObserver) this);
    }
  }
  @Override
  public void update(Account account) {

  }
}
