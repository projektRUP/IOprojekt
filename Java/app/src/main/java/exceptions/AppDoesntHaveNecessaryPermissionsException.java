package exceptions;

public class AppDoesntHaveNecessaryPermissionsException extends Exception {
    public AppDoesntHaveNecessaryPermissionsException(String errorMessage) {
        super(errorMessage);
    }

    public AppDoesntHaveNecessaryPermissionsException() {
        super();
    }
}
