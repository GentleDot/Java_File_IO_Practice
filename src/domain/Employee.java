package domain;

import java.util.Objects;

import static common.ParamCheckUtil.notNullCheck;
import static common.ParamCheckUtil.patternCheck;

public class Employee {
    private final String seq;
    private final String name;
    private final String phoneNumber;
    private final String ranks;
    private final String email;
    private EmployeeStatus status;

    public Employee(String seq) {
        this(seq, "default", "010-1234-5678", "사원", "default@email.com", EmployeeStatus.MEMBER);
    }

    public Employee(String seq, String name, String phoneNumber, String ranks, String email, EmployeeStatus status) {
        patternCheck("^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$", phoneNumber, "입력된 전화번호가 올바르지 않습니다.");
        patternCheck("^[\\d\\w-_.]+@[\\d\\w]+[.][\\w]{2,4}$", email, "입력된 이메일이 올바르지 않습니다.");
        notNullCheck(name, "이름은 빈 값이 될 수 없습니다.");
        notNullCheck(phoneNumber, "전화번호는 빈 값이 될 수 없습니다.");
        notNullCheck(ranks, "직급은 빈 값이 될 수 없습니다.");
        notNullCheck(email, "이메일은 빈 값이 될 수 없습니다.");

        this.seq = seq;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.ranks = ranks;
        this.email = email;
        this.status = status == null ? EmployeeStatus.MEMBER : status;
    }

    public String getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRanks() {
        return ranks;
    }

    public String getEmail() {
        return email;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringBuffer()
                .append(seq)
                .append(",")
                .append(name)
                .append(",")
                .append(phoneNumber)
                .append(",")
                .append(ranks)
                .append(",")
                .append(email)
                .append(",")
                .append(status.value())
                .append("\r\n").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return seq.equals(employee.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    public static final class Builder {
        private String seq;
        private String name;
        private String phoneNumber;
        private String ranks;
        private String email;
        private EmployeeStatus status;

        public Builder(String seq) {
            this.seq = seq;
        }

        public Builder(Employee employee) {
            this.seq = employee.seq;
            this.name = employee.name;
            this.phoneNumber = employee.phoneNumber;
            this.ranks = employee.ranks;
            this.email = employee.email;
            this.status = employee.status;
        }

        public Builder seq(String seq) {
            this.seq = seq;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder ranks(String ranks) {
            this.ranks = ranks;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder status(EmployeeStatus status) {
            this.status = status;
            return this;
        }

        public Employee build() {
            return new Employee(seq, name, phoneNumber, ranks, email, status);
        }
    }
}
