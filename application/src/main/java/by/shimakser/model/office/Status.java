package by.shimakser.model.office;

public enum Status {
    In_Process(""), Uploaded(""), Not_Loaded("");

    String pathForFile;

    Status(String pathForFile) {
        this.pathForFile = pathForFile;
    }

    public String getPathForFile() {
        return pathForFile;
    }

    public void setPathForFile(String pathForFile) {
        this.pathForFile = pathForFile;
    }
}
