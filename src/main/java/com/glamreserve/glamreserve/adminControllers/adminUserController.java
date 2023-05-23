package com.glamreserve.glamreserve.adminControllers;


import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@RestController
public class adminUserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/adminUsers")
    public ModelAndView adminUsers() {
        ModelAndView modelAndView = new ModelAndView("admin/adminUsers");
        User user = new User();
        modelAndView.addObject("user", user);
        List<User> users = userRepository.findAll();
        modelAndView.getModelMap().addAttribute("users", users);
        return modelAndView;
    }
    @PostMapping("/adminUserAdd")
    public ModelAndView adminUserAdd (@ModelAttribute("user") User user, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminUsers");
        User exitingUser = userRepository.findByUsername(user.getUsername());
        if(exitingUser==null) {
            userRepository.save(user);
            atri.addFlashAttribute("success", "Usuario guardado con éxito");
        }else {
            atri.addFlashAttribute("error", "Usuario no actualizado,El nombre de usuario ya existe");
        }
        return modelo;
    }

    @PostMapping("/adminUserUpdate")
    public ModelAndView adminUserUpdate (@ModelAttribute("user") User user, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminUsers");

        User userToUpdate = userRepository.getUserById(user.getId());
        userToUpdate.setName(user.getName());
        userToUpdate.setLastname(user.getLastname());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setRoleId(user.getRoleId());
        userToUpdate.setPhone(user.getPhone());
        if (!user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            userToUpdate.setPassword(encodedPassword);
        }
        userToUpdate.setUsername(user.getUsername());

        userRepository.save(userToUpdate);

            atri.addFlashAttribute("success", "Usuario actualizado con éxito");

            return modelo;
    }


   @GetMapping("/adminUserUpdateForm/{id}")
    public ModelAndView updateUser(@PathVariable(name="id") Long id, RedirectAttributes atri){
        ModelAndView modelo = new ModelAndView("admin/adminUserUpdateForm");
        User user = userRepository.getUserById(id);

       modelo.addObject("user",user);

        return modelo;
    }

    @GetMapping("/deleteUser/{id}")
    public ModelAndView deleteUser(@PathVariable(name="id") Long id, RedirectAttributes atri){

        User user = userRepository.findById(id).get();
        ModelAndView modelo = new ModelAndView("redirect:/admin/adminUsers");

            userRepository.deleteById(id);

        return modelo;
    }


}
