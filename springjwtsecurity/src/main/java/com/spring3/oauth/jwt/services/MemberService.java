package com.spring3.oauth.jwt.services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.repositories.MemberRepository;

@Service
public class MemberService {
      private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberInfo> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<MemberInfo> getMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public List<MemberInfo> searchByFirstName(String firstname) {
        return memberRepository.findByFirstname(firstname);
    }

    public List<MemberInfo> searchByLastName(String lastname) {
        return memberRepository.findByLastname(lastname);
    }

    public List<MemberInfo> searchByMiddleName(String middlename) {
        return memberRepository.findByMiddlename(middlename);
    }

    public List<MemberInfo> searchByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public List<MemberInfo> searchByBloodGroup(String bloodGroup) {
        return memberRepository.findByBloodGroup(bloodGroup);
    }

    public List<MemberInfo> searchByGender(String gender) {
        return memberRepository.findByGender(gender);
    }

    public List<MemberInfo> searchByDateOfBirth(Date dateOfBirth) {
        return memberRepository.findByDateOfBirth(dateOfBirth);
    }

    public MemberInfo saveMember(MemberInfo memberInfo) {
        return memberRepository.save(memberInfo);
    }

    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
    
}
