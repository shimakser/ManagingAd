package by.shimakser.office.exception;

public enum ExceptionOfficeText {
    NOT_FOUND("No items found."),
    FILE_NOT_FOUND("File is not found.");

    private String exceptionDescription;

    ExceptionOfficeText(String exceptionDescription) {
        this.exceptionDescription = exceptionDescription;
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }
}
