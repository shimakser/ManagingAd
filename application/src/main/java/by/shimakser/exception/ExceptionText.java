package by.shimakser.exception;

public enum ExceptionText {
    NotFound("No items found."),
    FileNotFound("File is not found."),
    EntityNotFound("Entity is not found."),

    AlreadyBound("Entered note is already taken."),

    JwtAuthentication("JWT token is expired or invalid."),
    AuthorizationService("Not authorized."),

    InsufficientRights("Insufficient rights to edit the user."),
    Authentication("Invalid email or password.");

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
