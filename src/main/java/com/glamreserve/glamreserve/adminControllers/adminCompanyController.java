package com.glamreserve.glamreserve.adminControllers;

import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.company.CompanyRepository;
import com.glamreserve.glamreserve.entities.schedule.Schedule;
import com.glamreserve.glamreserve.entities.schedule.ScheduleRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import com.glamreserve.glamreserve.entities.utils.GeocodingResponse;
import com.glamreserve.glamreserve.entities.utils.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@RestController
public class adminCompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${google.maps.api.key}")
    private String apiKey;

    @GetMapping("admin/adminCompanies")
    public ModelAndView adminCompanies() {
        ModelAndView modelAndView = new ModelAndView("admin/adminCompanies");
        Company company = new Company();
        modelAndView.addObject("company", company);
        List<Company> companies = companyRepository.findAll();
        modelAndView.getModelMap().addAttribute("companies", companies);
        return modelAndView;
    }

    @PostMapping("/adminCompanyAdd")
    public ModelAndView adminCompanyAdd (@ModelAttribute("company") Company company, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminCompanies");
        String address = company.getAddress();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                address.replace(" ", "+") + "&key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);
        if(company.getOwner() != null){
            User user = userRepository.findById(company.getOwner().getId()).get();
            user.setRoleId(2L);
            userRepository.save(user);
        }
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
        companyRepository.save(company);
            atri.addFlashAttribute("success", "Local guardado con éxito");

        return modelo;
    }

  @GetMapping("/adminCompanyUpdateForm/{id}")
    public ModelAndView updateCompany(@PathVariable(name="id") Long id){
        ModelAndView modelo = new ModelAndView("admin/adminCompanyUpdateForm");
       Company company = companyRepository.findById(id).get();

       modelo.addObject("company",company);

        return modelo;
    }

    @GetMapping("/deleteCompany/{id}")
    public ModelAndView deleteCompany(@PathVariable(name="id") Long id, RedirectAttributes atri){

         ModelAndView modelo = new ModelAndView("redirect:/admin/adminCompanies");

            companyRepository.deleteById(id);
        atri.addFlashAttribute("success", "Local eliminado con éxito");
        return modelo;
    }

    @PostMapping("/adminCompanyUpdate")
    public ModelAndView adminCompanyUpdate (@RequestBody Company company, RedirectAttributes atri){
        ModelAndView modelo = new ModelAndView("redirect:/admin/adminCompanies");

        Company companyToUpdate = companyRepository.findById(company.getId()).orElse(null);
        if ((companyToUpdate!=null)) {
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
                if (company.getImage() == null) {
                    company.setImage(companyToUpdate.getImage());
                }
                companyToUpdate = company;
                Company savedCompany = companyRepository.save(companyToUpdate);
            }

            atri.addFlashAttribute("success", "Local actualizado con éxito");
        }else{
            atri.addFlashAttribute("error", "No se ha podido actualizar");
        }


        return modelo;
    }

}
