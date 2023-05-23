package com.glamreserve.glamreserve.entities.schedule;


import com.glamreserve.glamreserve.entities.company.Company;
import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "companyid")
    private Company company;

    @Column(name = "dayofweek")
    private int dayOfWeek;

    @Column(name = "opentime1")
    private Time openTime1;

    @Column(name = "closetime1")
    private Time closeTime1;

    @Column(name = "opentime2")
    private Time openTime2;

    @Column(name = "closetime2")
    private Time closeTime2;

    public Schedule(){}

    public Schedule(Long id, Company company, int dayOfWeek, Time openTime1, Time closeTime1, Time openTime2, Time closeTime2) {
        this.id = id;
        this.company = company;
        this.dayOfWeek = dayOfWeek;
        this.openTime1 = openTime1;
        this.closeTime1 = closeTime1;
        this.openTime2 = openTime2;
        this.closeTime2 = closeTime2;
    }

    public Schedule(Company company, int dayOfWeek) {
        this.id = null;
        this.company = company;
        this.dayOfWeek = dayOfWeek;
    }

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

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Time getOpenTime1() {
        return openTime1;
    }

    public void setOpenTime1(Time openTime1) {
        this.openTime1 = openTime1;
    }

    public Time getCloseTime1() {
        return closeTime1;
    }

    public void setCloseTime1(Time closeTime1) {
        this.closeTime1 = closeTime1;
    }

    public Time getOpenTime2() {
        return openTime2;
    }

    public void setOpenTime2(Time openTime2) {
        this.openTime2 = openTime2;
    }

    public Time getCloseTime2() {
        return closeTime2;
    }

    public void setCloseTime2(Time closeTime2) {
        this.closeTime2 = closeTime2;
    }
}
