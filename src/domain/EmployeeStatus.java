package domain;

public enum EmployeeStatus {
    MEMBER("직원"), TERMINATED("퇴사");

    private String value;

    EmployeeStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
