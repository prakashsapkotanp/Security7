package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.repositories.MemberRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

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

    public MemberInfo updateMember(Long id, MemberInfo memberInfo) {
        MemberInfo existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));

        existingMember.setFirstname(memberInfo.getFirstname());
        existingMember.setMiddlename(memberInfo.getMiddlename());
        existingMember.setLastname(memberInfo.getLastname());
        existingMember.setDateOfBirth(memberInfo.getDateOfBirth());
        existingMember.setBloodGroup(memberInfo.getBloodGroup());
        existingMember.setGender(memberInfo.getGender());
        existingMember.setLastTimeOfDonation(memberInfo.getLastTimeOfDonation());
        existingMember.setRegistrationDate(memberInfo.getRegistrationDate());
        existingMember.setUserInfo(memberInfo.getUserInfo());
        existingMember.setMemberLocation(memberInfo.getMemberLocation());

        return memberRepository.save(existingMember);
    }

    public List<MemberInfo> findPotentialDonors(String bloodGroup, double latitude, double longitude, double radius) {
        return memberRepository.findByBloodGroupAndLocation(bloodGroup, latitude, longitude, radius);
    }
}
