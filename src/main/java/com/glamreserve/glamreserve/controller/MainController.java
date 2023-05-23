package com.glamreserve.glamreserve.controller;

import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.company.CompanyRepository;
import com.glamreserve.glamreserve.entities.reserve.Reserve;
import com.glamreserve.glamreserve.entities.reserve.ReserveRepository;
import com.glamreserve.glamreserve.entities.review.Review;
import com.glamreserve.glamreserve.entities.review.ReviewRepository;
import com.glamreserve.glamreserve.entities.schedule.Schedule;
import com.glamreserve.glamreserve.entities.schedule.ScheduleRepository;
import com.glamreserve.glamreserve.entities.schedule.ScheduleUtils;
import com.glamreserve.glamreserve.entities.service.Service;
import com.glamreserve.glamreserve.entities.service.ServiceRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import com.glamreserve.glamreserve.service.CompanyService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class MainController {

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
    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping(path = {"/", "/index"})
    public ModelAndView index() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }


    @GetMapping("/subscriptions")
    public ModelAndView subscriptions() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("subscriptions");
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "distance", required = false) Double radius,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {

        ModelAndView modelAndView = new ModelAndView("search");
        Pageable pageable = PageRequest.of(page, size);

        Page<Company> companies = CompanyService.search(name, category, latitude, longitude, radius, pageable);

        modelAndView.getModelMap().addAttribute("companies", companies.getContent());
        modelAndView.getModelMap().addAttribute("totalPages", companies.getTotalPages());
        modelAndView.getModelMap().addAttribute("currentPage", page);
        modelAndView.getModelMap().addAttribute("sizePerPage", size);

        return modelAndView;
    }

    @GetMapping("/viewCompany/{id}")
    public ModelAndView viewCompany(@PathVariable("id") Long companyId, HttpServletResponse response) {
        Optional<Company> company = companyRepository.findById(companyId);

        if (!company.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new ModelAndView("error");
        }

        List<Schedule> schedules = scheduleRepository.findByCompanyId(companyId);
        List<Service> services = serviceRepository.findByCompanyId(companyId);
        ModelAndView modelAndView = new ModelAndView("viewCompany");
        List<Date> unavailableDates = companyRepository.findUnavailableDatesByCompanyId(companyId);
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        modelAndView.addObject("company", company.get());
        modelAndView.addObject("schedules", schedules);
        modelAndView.addObject("services", services);
        modelAndView.addObject("scheduleUtils", new ScheduleUtils());
        modelAndView.addObject("unavailableDates", unavailableDates);
        modelAndView.addObject("reviews", reviews);
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;

    }

    @GetMapping("/registerCompany")
    public ModelAndView register2() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register2");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/error")
    public ModelAndView error() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        return modelAndView;
    }

    /* Busca usuari per id i mostrar la vista segons el tipus de rol si es rol 1 mostra el perfil d'usuari
     * si es rol 0=admin o 2=company mostra perfil empresa */
    @GetMapping("/main/{id}")
    public ModelAndView main(@PathVariable("id") Long userId, HttpServletResponse response) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new ModelAndView("error");
        }
        ModelAndView modelAndView = null;
        if (user.get().getRoleId() == 1) {
            List<Reserve> reserves = reserveRepository.findAllByUser_Id(userId);
            modelAndView = new ModelAndView("main");
            modelAndView.addObject("user", user.get());
            modelAndView.addObject("reserves", reserves);
        } else if (user.get().getRoleId() == 2) {
            // Renderitzar vista per a perfil de empresa
            modelAndView = new ModelAndView("companyProfile");
            modelAndView.addObject("user", user.get());

            List<Company> companies = companyRepository.findCompaniesByUser(Math.toIntExact(userId));
            Long companyId = companies.get(0).getId(); //agafem el primer de la llista ja que només es pot tenir una empresa
            Optional<Company> company = companyRepository.findById(companyId);
            List<Schedule> schedules = scheduleRepository.findByCompanyId(companyId);
            List<Service> services = serviceRepository.findByCompanyId(companyId);
            modelAndView.addObject("company", company.get());
            modelAndView.addObject("schedules", schedules);
            modelAndView.addObject("services", services);
            modelAndView.addObject("scheduleUtils", new ScheduleUtils());
        } else if (user.get().getRoleId() == 0) {
            modelAndView = new ModelAndView("admin");
        }
        return modelAndView;
    }

    @GetMapping("/edit-user/{id}")
    public ModelAndView EditUserForm(@PathVariable(name="id") Long id, RedirectAttributes atrib){
        ModelAndView modelo = new ModelAndView("edit-user");
        User user = userRepository.getUserById(id);
        modelo.addObject("user",user);
        return modelo;
    }
}
