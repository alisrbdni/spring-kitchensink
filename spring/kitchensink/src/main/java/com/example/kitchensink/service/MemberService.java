package com.example.kitchensink.service;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Validator validator; // If you need direct programmatic validation

    public Member register(@Valid Member member) {
        if (emailAlreadyExists(member.getEmail())) {
            throw new IllegalArgumentException("A member with this email already exists.");
        }
        return memberRepository.save(member);
    }

    public boolean emailAlreadyExists(String email) {
        return (memberRepository.findByEmail(email) != null);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(String id) {
        return memberRepository.findById(id);
    }
}
