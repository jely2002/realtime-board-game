package nl.hsleiden.ipsene.controllers;

import com.google.cloud.firestore.DocumentSnapshot;
import nl.hsleiden.ipsene.interfaces.Controller;
import nl.hsleiden.ipsene.interfaces.View;
import nl.hsleiden.ipsene.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class AccountController implements Controller {

  private static final Logger logger = LoggerFactory.getLogger(AccountController.class.getName());

  Account a = new Account();

  public AccountController() {}

  public boolean validateLogin(String uid, String pwd) {
    logger.info("validating login for account: {}", uid);
    return a.validateLogin(uid, pwd);
  }

  @Override
  public void update(DocumentSnapshot ds) {}

  @Override
  public void registerObserver(View v) {}
}
