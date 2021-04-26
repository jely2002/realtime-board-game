package nl.hsleiden.ipsene.models;

public class Account {

  private final String uid = "";
  private final String pwd = "";

  public Account() {}

  public boolean validateLogin(String uid, String pwd) {
    return (uid.equals(this.uid)) && (pwd.equals(this.pwd));
  }
}
