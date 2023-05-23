package com.glamreserve.glamreserve.controller;

import com.glamreserve.glamreserve.entities.company.Company;
import com.glamreserve.glamreserve.entities.company.CompanyRepository;
import com.glamreserve.glamreserve.entities.dto.AvailableSlot;
import com.glamreserve.glamreserve.entities.interceptor.ForbiddenAccessException;
import com.glamreserve.glamreserve.entities.reserve.Reserve;
import com.glamreserve.glamreserve.entities.reserve.ReserveRepository;
import com.glamreserve.glamreserve.entities.reserve.ReserveService;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import com.glamreserve.glamreserve.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/reserve")
public class ReserveController {
    @Autowired
    private ReserveRepository reserveRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> createReserve(@RequestHeader("Authorization") String token, @RequestBody Reserve reserve) {
        try{
            User user = AuthenticationService.getUserFromToken(token, userRepository);
            //comprovar que l'usuari sigui o bé propietari de l'empresa a la que correspon la reserva o l'usuari que va fer la reserva
            if (reserve.getCompany().getOwner() != null && !Objects.equals(user.getId(), reserve.getCompany().getOwner().getId()) && (reserve.getUser() != null && !Objects.equals(user.getId(), reserve.getUser().getId()))) {
                throw new ForbiddenAccessException("Acceso denegado: No eres dueño de esta empresa");
            }
            Reserve savedReserve = reserveRepo.save(reserve);
            return ResponseEntity.ok().body("Reserva creada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear la reserva.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReserve(@PathVariable("id") Long id) {
        try {
            Reserve reserve = reserveRepo.findById(id).orElseThrow();
            return new ResponseEntity<>(reserve, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva con id " + id + " no encontrada.");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReservesByUser(@RequestHeader("Authorization") String token, @PathVariable("userId") Long userId) {
        try {
            List<Reserve> reserves = reserveRepo.findAllByUser_Id(userId);
            return new ResponseEntity<>(reserves, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron reservas para el usuario con id " + userId);
        }
    }


    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getReservesByCompany(@PathVariable("companyId") Long companyId) {
        try {
            List<Reserve> reserves = reserveRepo.findAllByCompany_Id(companyId);
            return new ResponseEntity<>(reserves, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron reservas para la compañía con id " + companyId);
        }
    }

    @GetMapping("/companyDate/{companyId}/{date}")
    public ResponseEntity<?> getReservesByCompanyAndDate(@PathVariable("companyId") Long companyId, @PathVariable("date") String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            List<Reserve> reserves = reserveRepo.findByCompanyAndDate(companyId, date);
            return new ResponseEntity<>(reserves, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron reservas para la compañía con id " + companyId);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateReserve(@RequestHeader("Authorization") String token, @PathVariable("id") Long id, @RequestBody Reserve reserve) {
        try{
            Reserve originalReserve = reserveRepo.findById(id).orElseThrow();
            User user = AuthenticationService.getUserFromToken(token, userRepository);
            //comprovar que l'usuari sigui o bé propietari de l'empresa a la que correspon la reserva o l'usuari que va fer la reserva
            if (reserve.getCompany().getOwner() != null && !Objects.equals(user.getId(), reserve.getCompany().getOwner().getId()) && (reserve.getUser() != null && !Objects.equals(user.getId(), reserve.getUser().getId()))) {
                throw new ForbiddenAccessException("Acceso denegado: No eres dueño de esta empresa");
            }
            originalReserve.setUser(userRepo.findById(reserve.getUser().getId()).orElseThrow());
            originalReserve.setCompany(companyRepo.findById(reserve.getCompany().getId()).orElseThrow());
            originalReserve.setDate(reserve.getDate());
            originalReserve.setUserPhone(reserve.getUserPhone());
            originalReserve.setUserName(reserve.getUserName());
            Reserve updatedReserve = reserveRepo.save(originalReserve);
            return ResponseEntity.ok().body("Reserva editada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al editar la reserva.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReserve(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        try{
            Reserve reserve = reserveRepo.findById(id).orElseThrow();
            User user = AuthenticationService.getUserFromToken(token, userRepo);
            //comprovar que l'usuari sigui o bé propietari de l'empresa a la que correspon la reserva o l'usuari que va fer la reserva
            if (reserve.getCompany().getOwner() != null && !Objects.equals(user.getId(), reserve.getCompany().getOwner().getId()) && (reserve.getUser() != null && !Objects.equals(user.getId(), reserve.getUser().getId())))
            {
                throw new ForbiddenAccessException("Acceso denegado: No eres dueño de esta empresa");
            }
            reserveRepo.deleteById(id);
            return ResponseEntity.ok().body("Reserva eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar la reserva.");
        }
    }

    @GetMapping("/availableSlots")
    public List<AvailableSlot> getAvailableSlots(
            @RequestParam("date") String dateString,
            @RequestParam("companyId") Long companyId) {

        // parse the date string
        LocalDate date = LocalDate.parse(dateString);

        // fetch available slots
        List<AvailableSlot> availableSlots = reserveService.getAvailableSlots(date, companyId);

        return availableSlots;
    }
}