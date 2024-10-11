package hello.login.domain.hospital;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HospitalRepository {

    private static final Map<Long, Hospital> store = new HashMap<>(); //static
    private static long sequence = 0L; //static

    public Hospital save(Hospital hospital) {
        hospital.setId(++sequence);
        store.put(hospital.getId(), hospital);
        return hospital;
    }

    public Hospital findById(Long id) {
        return store.get(id);
    }

    public List<Hospital> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long hospitalId, Hospital updateParam) {
        Hospital findHospital = findById(hospitalId);
        findHospital.setHospitalName(updateParam.getHospitalName());
        findHospital.setHospitalAddress(updateParam.getHospitalAddress());
        findHospital.setHospitalHours(updateParam.getHospitalHours());
        findHospital.setHospitalNumber(updateParam.getHospitalNumber());
    }


    public void clearStore() {
        store.clear();
    }

}
