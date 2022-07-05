package com.example.springreact.jpa.repositories;
import com.example.springreact.jpa.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("SELECT c FROM Country  c WHERE c.name=:name")
    Country findByName(@Param("name") String name);
}
