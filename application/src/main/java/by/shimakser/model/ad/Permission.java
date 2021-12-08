package by.shimakser.model.ad;

public enum Permission {

    READ("user:read"),
    WRITE("user:write");
    private final String userPermission;

    Permission(String userPermission) {
        this.userPermission = userPermission;
    }

    public String getUserPermission() {
        return userPermission;
    }
}
