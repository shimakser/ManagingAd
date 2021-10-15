package by.shimakser.model.ad;

public enum Permission {

    READ("user:read"),
    WRITE("user:write");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
