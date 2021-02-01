package exceptions;

public class NoWhatsappInstalledException extends Exception {
    public NoWhatsappInstalledException(String errorMessage) {
        super(errorMessage);
    }

    public NoWhatsappInstalledException() {
        super();
    }
}
