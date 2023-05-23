package com.glamreserve.glamreserve.entities.review;


import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "companyid", referencedColumnName = "id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private User user;

    @Column(name = "score")
    private Integer score;

    @Column(name = "comment", length = 500)
    private String comment;


    // Constructors

    public Review() {
    }

    public Review(Company company, User user, Integer score, String comment) {
        this.company = company;
        this.user = user;
        this.score = score;
        this.comment = comment;
    }

    public Review(Long id, Company company, User user, Integer score, String comment) {
        this.id = id;
        this.company = company;
        this.user = user;
        this.score = score;
        this.comment = comment;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
