package com.glamreserve.glamreserve;

import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    private User user, user2, user3;

    @BeforeEach   //este método se ejecuta antes de cada uno de los demás
    void init(){
        //iniciamos 3 users
        user = new User();
        user.setName("Sara");
        user.setUsername("Sara");
        user.setPassword("123456789");
        user.setEmail("sara@sara.com");

        user2 = new User();
        user2.setName("Ares");
        user2.setUsername("Ares");
        user2.setPassword("123456789");
        user2.setEmail("ares@ares.com");

        user3 = new User();
        user3.setName("Dahlia");
        user3.setUsername("Dahlia");
        user3.setPassword("123456789");
        user3.setEmail("dahlia@dahlia.com");

    }
    @Test
    void AddUserTest(){
        User nuevo = userRepository.save(user);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
    }

    @Test
    void getUserListTest(){

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        List<User> users = userRepository.findAll();

        assertThat(users.size()).isEqualTo(3);


    }

    @Test
    void DeleteUserTest(){

        User nuevo = userRepository.save(user);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
        userRepository.deleteById(nuevo.getId());
        assertThat(userRepository.findByUsername(nuevo.getUsername())).isNull();

    }

    @Test
    void findByUsernameTest(){

        userRepository.save(user);

        User buscado = userRepository.findByUsername("Sara");
        assertEquals(buscado.getUsername(),"Sara");

    }

    @Test
    void getUserByIdTest(){

        userRepository.save(user);

        User buscado = userRepository.findById(user.getId()).get();
        assertThat(buscado).isNotNull();

    }


}
