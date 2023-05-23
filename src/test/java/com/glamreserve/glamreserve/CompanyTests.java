package com.glamreserve.glamreserve;

import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.company.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CompanyTests {

    @Autowired
    private CompanyRepository companyRepository;

    private Company company,company2,company3;

    @BeforeEach   //este método se ejecuta antes de cada uno de los demás
    void init(){
        //iniciarmos 3 locales
        company = new Company();
        company.setName("Local1");

        company2 = new Company();
        company2.setName("Local2");

        company3 = new Company();
        company3.setName("Local3");
    }

    @Test
    void AddCompanyTest(){
        Company nuevo = companyRepository.save(company);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
    }

    @Test
    void getCompanyListTest(){

        companyRepository.save(company);
        companyRepository.save(company2);
        companyRepository.save(company3);

        List<Company> companies = companyRepository.findAll();

        assertThat(companies.size()).isEqualTo(3);


    }

    @Test
    void DeleteCompanyTest(){

        Company nuevo = companyRepository.save(company);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
        companyRepository.deleteById(nuevo.getId());
        Optional<Company> borrado = companyRepository.findById(nuevo.getId());
        assertThat(borrado).isEmpty();

    }


    @Test
    void getCompanyByIdTest(){

        companyRepository.save(company);

        Company buscado = companyRepository.findById(company.getId()).get();
        assertThat(buscado).isNotNull();

    }


}
