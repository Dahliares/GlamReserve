package com.glamreserve.glamreserve.controller;

import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.company.CompanyRepository;
import com.glamreserve.glamreserve.entities.interceptor.ForbiddenAccessException;
import com.glamreserve.glamreserve.entities.schedule.Schedule;
import com.glamreserve.glamreserve.entities.schedule.ScheduleRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import com.glamreserve.glamreserve.service.AuthenticationService;
import com.glamreserve.glamreserve.service.CompanyService;
import com.glamreserve.glamreserve.entities.utils.GeocodingResponse;
import com.glamreserve.glamreserve.entities.utils.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    private final CompanyService companyService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    @Value("${google.maps.api.key}")
    private String apiKey;

    @PostMapping("/register")
    public ResponseEntity<String> registerCompany(@RequestHeader("Authorization") String token, @RequestBody Company company) {
        try{
            User authUser = AuthenticationService.getUserFromToken(token, userRepository);
            //comprovar que l'usuari no tingui ja una empresa (només es pot tenir una)
            List<Company> companies = companyRepository.findCompaniesByUser(Math.toIntExact(authUser.getId()));
            if (companies.size() > 0 || authUser.getRoleId() == 2) {
                throw new ForbiddenAccessException("Acceso denegado: No serás dueño de esta empresa o ya tienes una empresa");
            }
            String address = company.getAddress();
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                    address.replace(" ", "+") + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);
            User user = userRepository.findById(company.getOwner().getId()).get();
            user.setRoleId(2L);
            userRepository.save(user);
            if (response != null && response.getResults().size() > 0) {
                GeocodingResult result = response.getResults().get(0);
                double lat = result.getGeometry().getLocation().getLat();
                double lng = result.getGeometry().getLocation().getLng();
                company.setLatitude(lat);
                company.setLongitude(lng);
                Company savedCompany = companyRepository.save(company);
                //creem tots els horaris buits per la companyia per cada dia de la setmana
                for(int i = 1; i < 8; i++){
                    Schedule schedule = new Schedule(companyRepository.getCompanyById(savedCompany.getId()), i);
                    Schedule createdSchedule = scheduleRepository.save(schedule);
                }
                return ResponseEntity.ok("Entidad creada correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflicto al crear la compañia, tal vez este usuario ya tenga una asignada");
        }
    }


    @PutMapping("/edit")
    public ResponseEntity<?> editCompany(@RequestHeader("Authorization") String token, @RequestBody Company company) {
        try{
            Company existingCompany = companyRepository.findById(company.getId()).orElse(null);
            if (existingCompany == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            User authUser = AuthenticationService.getUserFromToken(token, userRepository);
            //comprovar que l'usuari sigui propietari d'una empresa
            if (!Objects.equals(authUser.getId(), company.getOwner().getId())) {
                throw new ForbiddenAccessException("Acceso denegado: No eres dueño de esta empresa.");
            }
            String address = company.getAddress();
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                    address.replace(" ", "+") + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);
            if (response != null && response.getResults().size() > 0) {
                GeocodingResult result = response.getResults().get(0);
                double lat = result.getGeometry().getLocation().getLat();
                double lng = result.getGeometry().getLocation().getLng();
                company.setLatitude(lat);
                company.setLongitude(lng);
                if (company.getImage() == null){
                    company.setImage(existingCompany.getImage());
                }
                existingCompany = company;
                Company savedCompany = companyRepository.save(existingCompany);
                return ResponseEntity.ok(savedCompany);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflicto al editar la compañia, tal vez este usuario ya tenga una asignada");
        }
    }

    @GetMapping("/{company_id}")
    public ResponseEntity<?> getReviewsByCompanyId(@PathVariable("company_id") Long companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam Optional<String> name,
                                @RequestParam Optional<String> category,
                                @RequestParam Optional<Double> latitude,
                                @RequestParam Optional<Double> longitude,
                                @RequestParam Optional<Double> radius,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> companies = CompanyService.search(name.orElse(null), category.orElse(null),
                latitude.orElse(null), longitude.orElse(null), radius.orElse(null), pageable);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{company_id}/unavailable")
    public ResponseEntity<?> getUnavailableDatesByCompanyId(@PathVariable("company_id") Long companyId) {
        List<Date> unavailableDates = companyRepository.findUnavailableDatesByCompanyId(companyId);
        return new ResponseEntity<>(unavailableDates, HttpStatus.OK);
    }

    @GetMapping("/{company_id}/availableSlots")
    public ResponseEntity<?> getAvailableSlots(@PathVariable("company_id") Long companyId, @RequestParam("date") Date date) {
        List<Time> unavailableDates = companyRepository.findAvailableSlotsByCompanyId(companyId, date);
        return new ResponseEntity<>(unavailableDates, HttpStatus.OK);
    }

}
