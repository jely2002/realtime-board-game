package nl.hsleiden.ipsene.exceptions;

public class ServerConnectionException extends Exception {

  private static final String ERROR_MESSAGE = "No connection with the server could be made.";

  public ServerConnectionException() {
    super(ERROR_MESSAGE);
  }
}
