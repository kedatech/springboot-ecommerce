package org.esfe.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.esfe.models.Product;
import org.esfe.models.User;
import org.esfe.services.interfaces.IProductService;
import org.esfe.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping ("/")
public class HomeController {

    @Autowired
    private IProductService productService;
    @Autowired
    private IUserService userService;

    @GetMapping
    public String index(OAuth2AuthenticationToken authentication, Model model) throws JsonProcessingException {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = (String) authentication.getPrincipal().getAttributes().get("name");
            model.addAttribute("username", username);
            String sub = (String) authentication.getPrincipal().getAttributes().get("sub");
            Optional<User> user = userService.buscarPorGoogleId(sub);
            user.ifPresent(value -> model.addAttribute("userId", value.getId()));
        } else {
            model.addAttribute("username", "");
            model.addAttribute("userId", 0);
        }

        List<Product> products = productService.obtenerTodos();
        List<Product> allproducts = productService.obtenerTodos();
        List<Product> productsWeek = products.stream()
                .limit(5) // Limita a los primeros 10 productos
                .collect(Collectors.toList());
        model.addAttribute("productsWeek", productsWeek);
        model.addAttribute("allproducts", allproducts);

        return "home/index";
    }


    // controlador healthcheck
    @GetMapping("/healthcheck")
    @ResponseBody
    public Object healthcheck(){
        return "ok";
    }
}

