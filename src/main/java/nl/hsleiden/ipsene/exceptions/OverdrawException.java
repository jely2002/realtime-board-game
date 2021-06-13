package nl.hsleiden.ipsene.exceptions;

public class OverdrawException extends Exception {

  public OverdrawException() {
    super("The game attempted to draw from an empty deck");
  }

  public OverdrawException(String message) {
    super(message);
  }
}
