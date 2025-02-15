package com.example.kitchensink;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Basic test verifying that the application can store a Member in MongoDB.
 */
@SpringBootTest
class MemberIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testRegisterMember() {
        Member m = new Member("Jane Doe", "jane@mail.com", "1234567890");
        Member saved = memberRepository.save(m);

        Assertions.assertNotNull(saved.getId(), "Saved member ID should not be null");
        Assertions.assertEquals("jane@mail.com", saved.getEmail());
    }
}
