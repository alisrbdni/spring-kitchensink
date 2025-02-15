package com.example.kitchensink.controller;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // GET all
    @GetMapping
    public List<Member> listAll() {
        return memberService.findAll();
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<Member> getById(@PathVariable String id) {
        return memberService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create
    @PostMapping
    public ResponseEntity<?> createMember(@Valid @RequestBody Member member) {
        try {
            Member saved = memberService.register(member);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            // handle uniqueness or other validation errors
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
