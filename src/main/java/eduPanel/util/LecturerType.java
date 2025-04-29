package eduPanel.util;

public enum LecturerType {
    FULL_TIME("full-time") , VISITING("visiting");
    private String type;

    LecturerType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
