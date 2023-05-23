package com.glamreserve.glamreserve;

import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.company.CompanyRepository;
import com.glamreserve.glamreserve.entities.service.Service;
import com.glamreserve.glamreserve.entities.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class ServiceTests {


    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ServiceRepository serviceRepository;


    private Company local = new Company();


    private Service service1,service2, service3;



    @BeforeEach   //este método se ejecuta antes de cada uno de los demás
    void init(){
        //iniciarmos 3 servicios

        service1 = new Service();
        service1.setName("service 1");

        service2 = new Service();
        service2.setName("service 2");

        service3 = new Service();
        service3.setName("service 3");
    }

    @Test
    void AddServiceTest(){
        Service service = serviceRepository.save(service1);
        assertNotNull(service);
        assertThat(service.getId()).isGreaterThan(0);
    }

    @Test
    void getServiceListTest(){

        serviceRepository.save(service1);
        serviceRepository.save(service2);
        serviceRepository.save(service3);

        List<Service> services = serviceRepository.findAll();

        assertThat(services.size()).isEqualTo(3);


    }

    @Test
    void DeleteServiceTest(){

        Service nuevo = serviceRepository.save(service1);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
        serviceRepository.deleteById(nuevo.getId());
        Optional<Service> borrado = serviceRepository.findById(nuevo.getId());
        assertThat(borrado).isEmpty();

    }


    @Test
    void getServiceByIdTest(){

        serviceRepository.save(service1);

        Service buscado = serviceRepository.findById(service1.getId()).get();
        assertThat(buscado).isNotNull();

    }

    @Test
    void findServiceByCompanyIdTest(){

        local.setName("Local1");
        Company guardada = companyRepository.save(local);
        service1.setCompany(guardada);
        Service guardado = serviceRepository.save(service1);

        List<Service> buscados = serviceRepository.findByCompanyId(guardada.getId());
        assertThat(buscados).isNotNull();



    }

}
