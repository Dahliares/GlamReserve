package com.glamreserve.glamreserve.entities.service;

import com.glamreserve.glamreserve.entities.reserve.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    List<Service> findByCompanyId(Long companyId);
}