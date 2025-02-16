package com.example.kitchensink.controller;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import com.example.kitchensink.service.MemberService; // Import MemberService
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MemberPageController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;  // Inject the MemberService

    @GetMapping("/members-ui")
    public String listMembers(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("newMember", new Member());
        return "members-list";
    }

    @PostMapping("/add-member")
    public String addMember(@Valid @ModelAttribute("newMember") Member member, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("members", memberRepository.findAll());
            return "members-list"; // Return to the form with errors
        }

        try {
            memberService.register(member); // Register via the service
            redirectAttributes.addFlashAttribute("message", "Member added successfully!"); //flash message
            return "redirect:/members-ui";
        } catch (RuntimeException e) {
            model.addAttribute("members", memberRepository.findAll());
            model.addAttribute("errorMessage", e.getMessage()); // Add error message to the model
            return "members-list"; // Return to the form with the error message
        }
    }
}