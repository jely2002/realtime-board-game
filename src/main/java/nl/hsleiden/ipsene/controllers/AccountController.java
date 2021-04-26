package nl.hsleiden.ipsene.controllers;

import nl.hsleiden.ipsene.models.Account;

public class AccountController {

  Account a = new Account();

  public AccountController() {}

  public boolean validateLogin(String uid, String pwd) {
    System.out.println("LoginController - Checking Login");

    return a.validateLogin(uid, pwd);
  }
}
