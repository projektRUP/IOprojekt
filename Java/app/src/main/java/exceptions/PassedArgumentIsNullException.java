package exceptions;

public class PassedArgumentIsNullException extends Exception {
    public PassedArgumentIsNullException(String errorMessage) {
        super(errorMessage);
    }

    public PassedArgumentIsNullException() {
        super();
    }
}
