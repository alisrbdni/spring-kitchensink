package com.example.kitchensink.controller;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import com.example.kitchensink.service.MemberService; // Import the service
import jakarta.validation.Valid; // Import for validation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NewUiController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService; // Inject the service


    /**
     * Displays the "modern" new UI at GET /new-ui.
     */
    @GetMapping("/new-ui")
    public String showNewUi(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("member", new Member()); // Add a member object for the form
        return "new-ui"; // references new-ui.html
    }

    /**
     * Handles form submission for the new UI.
     * POST /new-ui/add-member -> persist -> redirect back to /new-ui
     */
    @PostMapping("/new-ui/add-member")
    public String newUiAddMember(@Valid @ModelAttribute("member") Member member, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("members", memberRepository.findAll());
            return "new-ui"; // Return to the form with errors
        }

        try {
            memberService.register(member); // Use the service to register
            redirectAttributes.addFlashAttribute("message", "Member added successfully!");
            return "redirect:/new-ui";
        } catch (RuntimeException e) {
            model.addAttribute("members", memberRepository.findAll());
            model.addAttribute("errorMessage", e.getMessage()); // Add error message to the model
            return "new-ui"; // Return to the form with the error message
        }
    }
}