package com.example.kitchensink.controller;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberPageController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/members-ui")
    public String listMembers(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("newMember", new Member()); 
        return "members-list"; 
    }

    @PostMapping("/add-member")
    public String addMember(@ModelAttribute Member member) {
        memberRepository.save(member);
        return "redirect:/members-ui";
    }
}
