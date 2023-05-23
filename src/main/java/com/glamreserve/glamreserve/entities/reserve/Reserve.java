package com.glamreserve.glamreserve.entities.reserve;

import com.glamreserve.glamreserve.entities.service.Service;
import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.user.User;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reserves")
public class Reserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "companyid")
    private Company company;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "userphone")
    private String userPhone;

    @Column(name = "username")
    private String userName;

    @ManyToOne
    @JoinColumn(name = "service")
    private Service service;

    public Reserve() {}

    public Reserve(Long id, User user, Company company, Timestamp date, String userPhone, String userName, Service service) {
        this.id = id;
        this.user = user;
        this.company = company;
        this.date = date;
        this.userPhone = userPhone;
        this.userName = userName;
        this.service = service;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
