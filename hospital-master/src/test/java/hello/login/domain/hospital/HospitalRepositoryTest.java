package hello.login.domain.hospital;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HospitalRepositoryTest {

    HospitalRepository hospitalRepository = new HospitalRepository();

    @AfterEach
    void afterEach() {
        hospitalRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Hospital hospital = new Hospital("itemA", 10000, 10);

        //when
        Hospital savedHospital = hospitalRepository.save(hospital);

        //then
        Hospital findHospital = hospitalRepository.findById(hospital.getId());
        assertThat(findHospital).isEqualTo(savedHospital);
    }

    @Test
    void findAll() {
        //given
        Hospital hospital1 = new Hospital("item1", 10000, 10);
        Hospital hospital2 = new Hospital("item2", 20000, 20);

        hospitalRepository.save(hospital1);
        hospitalRepository.save(hospital2);

        //when
        List<Hospital> result = hospitalRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(hospital1, hospital2);
    }

    @Test
    void updateItem() {
        //given
        Hospital hospital = new Hospital("item1", 10000, 10);

        Hospital savedHospital = hospitalRepository.save(hospital);
        Long itemId = savedHospital.getId();

        //when
        Hospital updateParam = new Hospital("item2", 20000, 30);
        hospitalRepository.update(itemId, updateParam);

        Hospital findHospital = hospitalRepository.findById(itemId);

        //then
        assertThat(findHospital.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findHospital.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findHospital.getQuantity()).isEqualTo(updateParam.getQuantity());
    }
}
