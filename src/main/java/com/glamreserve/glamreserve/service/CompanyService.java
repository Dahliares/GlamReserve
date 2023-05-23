package com.glamreserve.glamreserve.service;

import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.company.CompanyRepository;
import com.glamreserve.glamreserve.entities.company.CompanySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private static CompanyRepository companyRepository = null;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public static Page<Company> search(String name, String category, Double latitude, Double longitude, Double radius, Pageable pageable) {
        Specification<Company> spec = CompanySpecification.filterByProperties(name, category, latitude, longitude, radius);
        return companyRepository.findAll(spec, pageable);
    }
}
