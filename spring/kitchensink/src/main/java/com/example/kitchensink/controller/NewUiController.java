package com.example.kitchensink.controller;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NewUiController {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Displays the "modern" new UI at GET /new-ui.
     */
    @GetMapping("/new-ui")
    public String showNewUi(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "new-ui"; // references new-ui.html
    }

    /**
     * Handles form submission for the new UI. 
     * POST /new-ui/add-member -> persist -> redirect back to /new-ui
     */
    @PostMapping("/new-ui/add-member")
    public String newUiAddMember(@ModelAttribute Member member) {
        memberRepository.save(member);
        // Return user back to the new UI page with updated list
        return "redirect:/new-ui";
    }
}
