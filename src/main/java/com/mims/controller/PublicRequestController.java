package com.mims.controller;

import com.mims.model.PublicRequest;
import com.mims.service.MimsDataService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PublicRequestController {

    private final MimsDataService dataService;

    public PublicRequestController(MimsDataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/request")
    public String requestForm(Model model) {
        model.addAttribute("request", new PublicRequest(null, null, null, null, null));
        model.addAttribute("domains", dataService.getDomains());
        return "request";
    }

    @PostMapping("/request")
    public String submitRequest(@Valid @ModelAttribute("request") PublicRequest request,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("domains", dataService.getDomains());
            return "request";
        }
        dataService.createPublicRequest(request);
        redirectAttributes.addFlashAttribute("submitted", true);
        return "redirect:/request";
    }
}
