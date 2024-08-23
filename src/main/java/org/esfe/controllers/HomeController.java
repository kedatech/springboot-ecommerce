package org.esfe.controllers;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/")
public class HomeController {
    @GetMapping
    public String index(OAuth2AuthenticationToken authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = (String) authentication.getPrincipal().getAttributes().get("name");
            model.addAttribute("username", username);
        } else {
            model.addAttribute("username", "");
        }
        return "home/index";
    }

}

