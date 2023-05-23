package com.glamreserve.glamreserve;

import com.glamreserve.glamreserve.entities.roles.Role;
import com.glamreserve.glamreserve.entities.roles.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class RoleTests {

    @Autowired
    private RoleRepository roleRepository;

    private Role role1, role2,role3;

    @BeforeEach   //este método se ejecuta antes de cada uno de los demás
    void init(){
        //iniciarmos 3 locales
        role1 = new Role();
        role1.setDescription("rol 1");

        role2 = new Role();
        role2.setDescription("rol 2");

        role3 = new Role();
        role3.setDescription("rol 3");
    }

    @Test
    void AddRoleTest(){
        Role role = roleRepository.save(role1);
        assertNotNull(role);
        assertThat(role.getId()).isGreaterThan(0);
    }

    @Test
    void getRoleListTest(){

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);

        List<Role> roles = roleRepository.findAll();

        assertThat(roles.size()).isEqualTo(3);


    }

    @Test
    void DeleteRoleTest(){

        Role nuevo = roleRepository.save(role1);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
        roleRepository.deleteById(nuevo.getId());
        Optional<Role> borrado = roleRepository.findById(nuevo.getId());
        assertThat(borrado).isEmpty();

    }


    @Test
    void getRoleByIdTest(){

        roleRepository.save(role1);

        Role buscado = roleRepository.findById(role1.getId()).get();
        assertThat(buscado).isNotNull();

    }


}
