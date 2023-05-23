package com.glamreserve.glamreserve;

import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.company.CompanyRepository;
import com.glamreserve.glamreserve.entities.contact.Contact;
import com.glamreserve.glamreserve.entities.review.Review;
import com.glamreserve.glamreserve.entities.review.ReviewRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewTests {

    @Autowired
    private ReviewRepository reviewRepository;


    private Review review, review1, review2;


    @BeforeEach   //este método se ejecuta antes de cada uno de los demás
    void init(){

        //iniciamos 3 contactos
        review = new Review();
        review1 = new Review();
        review2 = new Review();

    }
    @Test
    void AddReviewTest(){
        Review nuevo = reviewRepository.save(review);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
    }

    @Test
    void getReviewListTest(){

        reviewRepository.save(review);
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<Review> users = reviewRepository.findAll();

        assertThat(users.size()).isEqualTo(3);


    }

    @Test
    void DeleteReviewTest(){

        Review nuevo = reviewRepository.save(review);
        assertNotNull(nuevo);
        assertThat(nuevo.getId()).isGreaterThan(0);
        reviewRepository.delete(nuevo);
        assertThat(reviewRepository.findById(nuevo.getId()).isEmpty());

    }


    @Test
    void getReviewByIdTest(){

        reviewRepository.save(review);

        Review buscado = reviewRepository.findById(review.getId()).get();
        assertThat(buscado).isNotNull();

    }


}
