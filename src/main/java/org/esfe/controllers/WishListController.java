package org.esfe.controllers;

import org.esfe.models.Wishlist;
import org.esfe.services.interfaces.IWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/wishlist")
public class WishListController {
    @Autowired
    private IWishlistService wishlistService;

    public String index(Model model){
        List<Wishlist> wishlists = wishlistService.obtenerTodos();
        model.addAttribute("wishlist", wishlists);
        return "wishlist/index";
    }

    @GetMapping("/create")
    public String create(Wishlist wishlist){
        return "wishlist/create";
    }

    @PostMapping("/save")
    public String save(Wishlist wishlist, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            model.addAttribute(wishlist);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "wishlist/create";
        }

        wishlistService.createOEditar(wishlist);
        attributes.addFlashAttribute("msg", "La Wishlist creado correctamente");
        return "redirect:/wishlist";
    }

    @GetMapping("/obtener/{id}")
    public String obtener(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes){
        Wishlist wishlist = wishlistService.buscarPorId(id).orElse(null);
        if (wishlist != null){
            model.addAttribute("wishlist", wishlist);
            return "wishlist/detail";
        }else {
            attributes.addFlashAttribute("error", "Wishlist no encontrada");
            return "redirect:/wishlist";
        }
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        Wishlist wishlist = wishlistService.buscarPorId(id).get();
        model.addAttribute("wishlist", wishlist);
        return "wishlist/delete";
    }

    @PostMapping("/delete")
    public String delete(Wishlist wishlist, RedirectAttributes attributes){
        wishlistService.eliminarPorId(wishlist);
        attributes.addFlashAttribute("msg", "La Wishlist eliminado correctamente");
        return "redirect:/wishlist";
    }

}
