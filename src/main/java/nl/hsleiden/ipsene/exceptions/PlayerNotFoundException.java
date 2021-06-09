package nl.hsleiden.ipsene.exceptions;

public class PlayerNotFoundException extends Exception {
  public PlayerNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
