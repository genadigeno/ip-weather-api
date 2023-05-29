package com.geno.weather.repository;

import com.geno.weather.data.IPAddressRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPAddressRequestRepository extends JpaRepository<IPAddressRequest, Integer> {
}
