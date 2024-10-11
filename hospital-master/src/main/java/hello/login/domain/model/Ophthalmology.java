package hello.login.domain.model;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ophthalmology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ophthalmology_id")
    private Long id;

    private String name; // 안과 이름
    private String address; // 안과 주소
    private String number; // 안과 전화번호
//    private String hours;
    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "ophthalmology", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

}
