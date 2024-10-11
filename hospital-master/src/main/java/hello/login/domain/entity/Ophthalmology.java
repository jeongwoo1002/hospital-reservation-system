package hello.login.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class Ophthalmology {

    private String name;
    private String address;
    private String number;
    private String hours;
}
