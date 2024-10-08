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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.obtenerTodos();
        model.addAttribute("users", users);
        return "user/index";
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

    @GetMapping("/all")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.obtenerTodos();
    }


    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/create";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.createOEditar(user);
        return "redirect:/users";
    }

    @PostMapping("/saveadmin")
    public String saveAdminUser(@ModelAttribute("user") User user) {
        user.setAdmin(true);
        userService.createOEditar(user);
        return "redirect:/users";
    }
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Integer id, Model model) {
        Optional<User> user = userService.buscarPorId(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/edit";
        } else {
            return "redirect:/users";
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute("user") User user) {
        Optional<User> existentUser = userService.buscarPorId(id);
        if (existentUser.isPresent()) {
            User existingUser = existentUser.get();
            existingUser.setAdmin(user.isAdmin());
            userService.createOEditar(existingUser);
        } else {
            return "redirect:/error"; //enviar a una ruta de error :V
        }

        return "redirect:/users";
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.eliminarPorId(id);
        return "redirect:/users";
    }
}
