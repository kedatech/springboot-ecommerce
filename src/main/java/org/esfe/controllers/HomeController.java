package org.esfe.controllers;

import org.esfe.models.Product;
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

@Controller
@RequestMapping ("/")
public class HomeController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public String index(OAuth2AuthenticationToken authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = (String) authentication.getPrincipal().getAttributes().get("name");
            // imprimir el nombre del usuario en la consola
            System.out.println("Usuario autenticado: " + username);
            model.addAttribute("username", username);
        } else {
            model.addAttribute("username", "");
        }
        List<Product> productsWeek = productService.obtenerTodos();
        model.addAttribute("productsWeek", productsWeek);

        return "home/index";
    }

    // controlador healthcheck
    @GetMapping("/healthcheck")
    @ResponseBody
    public Object healthcheck(){
        return "ok";
    }
}

