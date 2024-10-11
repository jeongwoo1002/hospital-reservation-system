package hello.login.domain.repository;

import hello.login.domain.model.Ophthalmology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OphthalmologyRepository extends JpaRepository<Ophthalmology, Long> {
    Page<Ophthalmology> findAll(Pageable pageable);
    Page<Ophthalmology> findByAddressContaining(String address, Pageable pageable);

    @Query(value = "SELECT * FROM Ophthalmology o WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(o.latitude)) * cos(radians(o.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(o.latitude)))) < :radius",
            nativeQuery = true)
    List<Ophthalmology> findNearby(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radius);
}