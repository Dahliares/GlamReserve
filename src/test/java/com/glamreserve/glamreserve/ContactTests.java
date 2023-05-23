package com.glamreserve.glamreserve;

import com.glamreserve.glamreserve.entities.contact.Contact;
import com.glamreserve.glamreserve.entities.contact.ContactRepository;
import com.glamreserve.glamreserve.entities.user.User;
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
public class ContactTests {

    @Autowired
    private ContactRepository contactRepository;

    private Contact contact, contact1, contact2;

    @BeforeEach   //este método se ejecuta antes de cada uno de los demás
    void init(){
        //iniciamos 3 contactos
        contact = new Contact();
        contact.setName("Sara");
        contact.setMessage("hola");
        contact.setPhone("456546");
        contact.setEmail("sara@sara.com");

        contact1 = new Contact();
        contact1.setName("Ares");
        contact1.setEmail("ares@ares.com");

        contact2 = new Contact();
        contact2.setName("Dahlia");
        contact2.setEmail("dahlia@dahlia.com");

    }
    @Test
    void AddContactTest(){
        Contact nuevo = contactRepository.save(contact);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
    }

    @Test
    void getContactListTest(){

        contactRepository.save(contact);
        contactRepository.save(contact1);
        contactRepository.save(contact2);

        List<Contact> users = contactRepository.findAll();

        assertThat(users.size()).isEqualTo(3);


    }

    @Test
    void DeleteContactTest(){

        Contact nuevo = contactRepository.save(contact);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
        contactRepository.deleteById(nuevo.getId());
        assertThat(contactRepository.findById(nuevo.getId())).isEmpty();

    }


    @Test
    void getContactByIdTest(){

        contactRepository.save(contact);

        Contact buscado = contactRepository.findById(contact.getId()).get();
        assertThat(buscado).isNotNull();

    }


}
