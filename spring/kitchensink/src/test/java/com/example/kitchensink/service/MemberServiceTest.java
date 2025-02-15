package com.example.kitchensink.service;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterNewMember() {
        Member member = new Member("John Doe", "john.doe@example.com", "1234567890");
        when(memberRepository.save(member)).thenReturn(member);

        Member registeredMember = memberService.register(member);

        assertNotNull(registeredMember);
        assertEquals("John Doe", registeredMember.getName());
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    void testRegisterExistingEmail() {
        Member member = new Member("Jane Doe", "jane.doe@example.com", "0987654321");
        when(memberRepository.findByEmail("jane.doe@example.com")).thenReturn(member);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            memberService.register(member);
        });

        assertEquals("Email taken", exception.getMessage());
    }

    @Test
    void testFindAllMembers() {
        memberService.findAll();
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testFindMemberById() {
        Member member = new Member("John Doe", "john.doe@example.com", "1234567890");
        when(memberRepository.findById("1")).thenReturn(Optional.of(member));

        Optional<Member> foundMember = memberService.findById("1");

        assertTrue(foundMember.isPresent());
        assertEquals("John Doe", foundMember.get().getName());
    }
}
