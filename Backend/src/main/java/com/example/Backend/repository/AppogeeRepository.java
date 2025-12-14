package com.example.Backend.repository;

import com.example.Backend.model.Appogee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AppogeeRepository extends JpaRepository<Appogee, Long> {
    Appogee findByNumAppogee(String numAppogee);

    @Query(value = "SELECT * FROM appogee WHERE num_appogee = :num", nativeQuery = true)
    List<Appogee> findNativeByNumAppogee(@Param("num") String num);
}