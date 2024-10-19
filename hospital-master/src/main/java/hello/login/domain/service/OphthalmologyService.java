package hello.login.domain.service;

import hello.login.domain.model.Ophthalmology;
import hello.login.domain.repository.OphthalmologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OphthalmologyService {

    private final OphthalmologyRepository ophthalmologyRepository;

    public List<Ophthalmology> getAllOphthalmologies() {
        return ophthalmologyRepository.findAll();
    }

    public Page<Ophthalmology> getAllOphthalmologies(Pageable pageable) {
        return ophthalmologyRepository.findAll(pageable);
    }
    public Ophthalmology getOphthalmologyById(Long id) {
        return ophthalmologyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Ophthalmology ID"));
    }

    public Page<Ophthalmology> searchByAddress(String address, Pageable pageable) {
        return ophthalmologyRepository.findByAddressContaining(address, pageable);
    }

    public List<Ophthalmology> findNearbyOphthalmologies(double lat, double lng) {
        double radius = 10.0;
        return ophthalmologyRepository.findNearby(lat, lng, radius);
    }

}
