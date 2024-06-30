package com.spring3.oauth.jwt.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring3.oauth.jwt.models.MemberInfo;

@Repository
public interface MemberRepository extends JpaRepository<MemberInfo,Long> {
    
    List<MemberInfo> findByFirstname(String firstname);

    List<MemberInfo> findByLastname(String lastname);

    List<MemberInfo> findByMiddlename(String middlename);

    List<MemberInfo> findByBloodGroup(String bloodGroup);

    List<MemberInfo> findByGender(String gender);

    List<MemberInfo> findByDateOfBirth(Date dateOfBirth);
    
}
