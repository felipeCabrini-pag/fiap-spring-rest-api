package br.com.fiap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "admin";
    }
}