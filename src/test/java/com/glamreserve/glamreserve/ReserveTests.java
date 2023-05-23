package com.glamreserve.glamreserve;

import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.company.CompanyRepository;
import com.glamreserve.glamreserve.entities.reserve.Reserve;
import com.glamreserve.glamreserve.entities.reserve.ReserveRepository;
import com.glamreserve.glamreserve.entities.reserve.ReserveService;
import com.glamreserve.glamreserve.entities.service.Service;
import com.glamreserve.glamreserve.entities.service.ServiceRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
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
public class ReserveTests {

    @Autowired
    private ReserveRepository reserveRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    private Reserve reserve, reserve1, reserve2;


    private User user;
    private Service service;
    private Company company;

    @BeforeEach   //este método se ejecuta antes de cada uno de los demás
    void init(){

       company = new Company();
        company.setName("Local");
        companyRepository.save(company);
        user = new User();
        user.setUsername("Sara");
        user.setPassword("123456789");
        user.setEmail("sara@sara.com");
        userRepository.save(user);
        service = new Service();
        service.setName("servicio");
        service.setCompany(company);
        serviceRepository.save(service);
        //iniciarmos 3 reservas
        reserve = new Reserve();
        reserve.setCompany(company);
        reserve.setUser(user);
        reserve.setService(service);

        reserve1 = new Reserve();

        reserve2 = new Reserve();
    }

    @Test
    void AddReserveTest(){
        Reserve nuevo = reserveRepository.save(reserve);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
    }

    @Test
    void getReserveListTest(){

        reserveRepository.save(reserve);
        reserveRepository.save(reserve1);
        reserveRepository.save(reserve2);

        List<Reserve> companies = reserveRepository.findAll();

        assertThat(companies.size()).isEqualTo(3);


    }

    @Test
    void DeleteReserveTest(){

        Reserve nuevo = reserveRepository.save(reserve);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
        reserveRepository.deleteById(nuevo.getId());
        Optional<Reserve> borrado = reserveRepository.findById(nuevo.getId());
        assertThat(borrado).isEmpty();

    }


    @Test
    void getReserveyByIdTest(){

        reserveRepository.save(reserve);

        Reserve buscado = reserveRepository.findById(reserve.getId()).get();
        assertThat(buscado).isNotNull();

    }

    @Test
    void findAllByCompany_IdTest(){
        Reserve guardada = reserveRepository.save(reserve);

        List<Reserve> listaReservas = reserveRepository.findAllByCompany_Id(guardada.getCompany().getId());
        assertThat(listaReservas).isNotNull();
        assertEquals(listaReservas.get(0).getCompany().getName(),"Local");
    }

    @Test
    void findAllByUser_IdTest(){

        Reserve guardada = reserveRepository.save(reserve);

        List<Reserve> listaReservas = reserveRepository.findAllByUser_Id(guardada.getUser().getId());
        assertThat(listaReservas).isNotNull();
        assertEquals(listaReservas.get(0).getUser().getUsername(),"Sara");

    }


}
