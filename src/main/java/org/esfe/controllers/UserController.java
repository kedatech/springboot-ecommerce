package org.esfe.controllers;

import org.esfe.models.User;
import org.esfe.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/manage")
    public String listUsers(Model model) {
        List<User> users = userService.obtenerTodos();
        model.addAttribute("users", users);
        return "user/index";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Integer id, Model model) {
        Optional<User> user = userService.buscarPorId(id);
        if (user.isPresent()) {
            model.addAttribute("usuario", user.get());
            return "user/detail";
        } else {
            return "redirect:/user";
        }
    }

    @GetMapping("/me")
    public Object getUserByGoogleId(OAuth2AuthenticationToken authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String googleId = (String) authentication.getPrincipal().getAttributes().get("sub"); // Obtén el ID del usuario desde el token
            Optional<User> user = userService.buscarPorGoogleId(googleId); // Necesitarás implementar este método en tu servicio
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "user/me";
            } else {
                return "redirect:/home/index";
            }
        } else {
            return "redirect:/login"; // Redirige al login si no está autenticado
        }
    }

    // me-json
    @GetMapping("/me-json")
    @ResponseBody
    public Object getUserByGoogleIdJson(OAuth2AuthenticationToken authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String googleId = (String) authentication.getPrincipal().getAttributes().get("sub"); // Obtén el ID del usuario desde el token
            Optional<User> user = userService.buscarPorGoogleId(googleId); // Necesitarás implementar este método en tu servicio
            if (user.isPresent()) {
                return user.get();
            } else {
                return "Usuario no encontrado";
            }
        } else {
            return "No autenticado";
        }
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/create";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.createOEditar(user);
        return "redirect:/user";
    }

    @PostMapping("/saveadmin")
    public String saveAdminUser(@ModelAttribute("user") User user) {
        user.setAdmin(true);
        userService.createOEditar(user);
        return "redirect:/user";
    }
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Integer id, Model model) {
        Optional<User> user = userService.buscarPorId(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/form";
        } else {
            return "redirect:/user";
        }
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Optional<User> user = userService.buscarPorId(id);
        if(user.isPresent()){
            model.addAttribute("user", user.get());
            return "user/detail";
        }

        return "redirect:/user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute("user") User user) {
        user.setId(id);
        userService.createOEditar(user);
        return "redirect:/user";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.eliminarPorId(id);
        return "redirect:/user";
    }
}
