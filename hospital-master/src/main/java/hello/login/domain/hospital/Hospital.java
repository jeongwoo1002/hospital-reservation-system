package hello.login.domain.hospital;

import lombok.Data;

@Data
public class Hospital {

    private Long id;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalHours;
    private String hospitalNumber;

    public Hospital() {
    }

    public Hospital(String hospitalName, String hospitalAddress, String hospitalHours, String hospitalNumber) {
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.hospitalHours = hospitalHours;
        this.hospitalNumber = hospitalNumber;
    }
}