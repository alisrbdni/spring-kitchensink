package com.example.kitchensink.repository;

import com.example.kitchensink.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    void testSaveAndFindByEmail() {
        Member member = new Member("Alice", "alice@example.com", "1234567890");
        memberRepository.save(member);

        Member foundMember = memberRepository.findByEmail("alice@example.com");
        assertNotNull(foundMember);
        assertEquals("Alice", foundMember.getName());
    }

    @Test
    void testFindById() {
        Member member = new Member("Bob", "bob@example.com", "0987654321");
        member = memberRepository.save(member);

        Member foundMember = memberRepository.findById(member.getId()).orElse(null);
        assertNotNull(foundMember);
        assertEquals("Bob", foundMember.getName());
    }

    @Test
    void testFindAll() {
        Member member1 = new Member("Charlie", "charlie@example.com", "1111111111");
        Member member2 = new Member("David", "david@example.com", "2222222222");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Iterable<Member> members = memberRepository.findAll();
        assertEquals(2, ((Collection<?>) members).size());
    }
}
