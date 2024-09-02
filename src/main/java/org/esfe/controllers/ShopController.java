package org.esfe.controllers;

import org.esfe.models.Category;
import org.esfe.models.Product;
import org.esfe.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private IProductService productService;

    @GetMapping("/product/{id}")
    public String detailProduct(@PathVariable("id") Integer id, Model model){
        Optional<Product> product = productService.buscarPorId(id);
        model.addAttribute("product", product.get());
        return "shop/product";
    }
}
