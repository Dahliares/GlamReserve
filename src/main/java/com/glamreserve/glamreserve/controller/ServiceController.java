package com.glamreserve.glamreserve.controller;

import com.glamreserve.glamreserve.entities.interceptor.ForbiddenAccessException;
import com.glamreserve.glamreserve.entities.service.Service;
import com.glamreserve.glamreserve.entities.service.ServiceRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import com.glamreserve.glamreserve.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    //els gets de servei són sempre públicament accessibles
    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable("id") int id) {
        Optional<Service> optionalServiceEntity = serviceRepository.findById(id);
        if (optionalServiceEntity.isPresent()) {
            return new ResponseEntity<>(optionalServiceEntity.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Service> createService(@RequestHeader("Authorization") String token, @RequestBody Service serviceEntity) {
        User user = AuthenticationService.getUserFromToken(token, userRepository);

        // Comprovar que l'usuari sigui admin o empresa
        if (!(user.getRoleId() == 0 || user.getRoleId() == 2)) {
            throw new ForbiddenAccessException("Acceso denegado: No tienes un rol de admin o empresa.");
        }

        Service newServiceEntity = serviceRepository.save(serviceEntity);
        return new ResponseEntity<>(newServiceEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@RequestHeader("Authorization") String token, @PathVariable("id") int id, @RequestBody Service serviceEntity) {
        Optional<Service> optionalServiceEntity = serviceRepository.findById(id);
        if (optionalServiceEntity.isPresent()) {
            User user = AuthenticationService.getUserFromToken(token, userRepository);
            //comprovar que l'usuari sigui propietari de l'empresa a la que correspon el servei
            if (!Objects.equals(user.getId(), optionalServiceEntity.get().getCompany().getOwner().getId())) {
                throw new ForbiddenAccessException("Acceso denegado: No eres dueño de esta empresa");
            }
            Service updatedServiceEntity = optionalServiceEntity.get();
            updatedServiceEntity = serviceEntity;
            serviceRepository.save(updatedServiceEntity);
            return new ResponseEntity<>(updatedServiceEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@RequestHeader("Authorization") String token, @PathVariable("id") int id) {
        Optional<Service> optionalServiceEntity = serviceRepository.findById(id);
        if (optionalServiceEntity.isPresent()) {
            User user = AuthenticationService.getUserFromToken(token, userRepository);
            //comprovar que l'usuari sigui propietari de l'empresa a la que correspon el servei
            if (!Objects.equals(user.getId(), optionalServiceEntity.get().getCompany().getOwner().getId())) {
                throw new ForbiddenAccessException("Acceso denegado: No eres dueño de esta empresa");
            }
            serviceRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
