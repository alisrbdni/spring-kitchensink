package com.example.kitchensink.repository;

import com.example.kitchensink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends MongoRepository<Member, String> {

    // @Indexed(unique = true)
    Member findByEmail(String email);

    @Query("{ 'email' : :#{#email} }")
    Member findMemberByEmail(@Param("email") String email);

    // Additional custom queries can go here
}