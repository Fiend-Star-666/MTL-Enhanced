package com.astoria.mtldataconvert.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

    @GetMapping("/myview")
    public String myView(Model model) {
        // You can add attributes to the model that will be accessible in the HTML view
        model.addAttribute("message", "Hello from the controller!");
        return "myview"; // This should match the name of your HTML file (without .html)
    }


}
