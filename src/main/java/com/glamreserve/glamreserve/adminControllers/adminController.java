package com.glamreserve.glamreserve.adminControllers;

import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.company.CompanyRepository;
import com.glamreserve.glamreserve.entities.reserve.ReserveRepository;
import com.glamreserve.glamreserve.entities.schedule.ScheduleRepository;
import com.glamreserve.glamreserve.entities.service.ServiceRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;



@RestController
public class adminController{

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReserveRepository reserveRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/admin")
    public ModelAndView admin() {
        ModelAndView modelAndView = new ModelAndView("admin");

        return modelAndView;
    }



}
