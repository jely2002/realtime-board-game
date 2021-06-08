package nl.hsleiden.ipsene.exceptions;

public class GameNotFoundException extends Exception {
    public GameNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
