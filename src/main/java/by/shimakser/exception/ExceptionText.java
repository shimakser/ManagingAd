package by.shimakser.exception;

public enum ExceptionText {
    NotFound("Not found."),
    AlreadyBound("Entered note is already taken."),
    Authentication("Insufficient rights to edit the user."),
    AuthorizationService("Not authorized.");

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
