package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.models.Account;
import nl.hsleiden.ipsene.views.View;

public class AccountController implements Controller {

  Account a = new Account();

  public AccountController() {}

  public boolean validateLogin(String uid, String pwd) {
    System.out.println("LoginController - Checking Login");

    return a.validateLogin(uid, pwd);
  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {}
}
