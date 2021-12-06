package by.shimakser.exception;

public enum ExceptionText {
    NOT_FOUND("No items found."),
    FILE_NOT_FOUND("File is not found."),
    ENTITY_NOT_FOUND("Entity is not found."),

    ALREADY_BOUND("Entered note is already taken."),

    JWT_AUTHENTICATION("JWT token is expired or invalid."),
    AUTHORIZATION_SERVICE("Not authorized."),

    INSUFFICIENT_RIGHTS("Insufficient rights to edit the user."),
    AUTHENTICATION("Invalid email or password.");

    private String exceptionText;

    ExceptionText(String exceptionText) {
        this.exceptionText = exceptionText;
    }

    public String getExceptionText() {
        return exceptionText;
    }

    public void setExceptionText(String exceptionText) {
        this.exceptionText = exceptionText;
    }
}
