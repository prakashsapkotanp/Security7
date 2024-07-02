package com.spring3.oauth.jwt.services;

import java.util.List;
import java.util.Optional;

import com.spring3.oauth.jwt.repositories.MemberRepository;
import com.spring3.oauth.jwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.models.MemberLocation;
import com.spring3.oauth.jwt.models.UserInfo;

import com.spring3.oauth.jwt.repositories.MemberLocationRepository;


@Service
public class MemberLocationService {

    private final MemberLocationRepository memberLocationRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @Autowired
    public MemberLocationService(MemberLocationRepository memberLocationRepository,
                                 MemberRepository memberRepository,
                                 UserRepository userRepository) {
        this.memberLocationRepository = memberLocationRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    public List<MemberLocation> getAllMemberLocations() {
        return memberLocationRepository.findAll();
    }

    public Optional<MemberLocation> getMemberLocationById(Long id) {
        return memberLocationRepository.findById(id);
    }

    public MemberLocation saveMemberLocation(MemberLocation memberLocation) {
        MemberInfo memberInfo = memberLocation.getMemberInfo();

        if (memberInfo != null && memberInfo.getUserInfo() != null) {
            // Check if the UserInfo exists
            Long userId = memberInfo.getUserInfo().getId();
            UserInfo userInfo = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Could not found user..!!"));

            memberInfo.setUserInfo(userInfo);

            // If the MemberInfo already has an ID, verify it exists
            if (memberInfo.getId() != null) {
                memberInfo = memberRepository.findById(memberInfo.getId())
                        .orElseThrow(() -> new RuntimeException("Member not found"));
            }

            memberLocation.setMemberInfo(memberInfo);
        }

        return memberLocationRepository.save(memberLocation);
    }

    public void deleteMemberLocation(Long id) {
        memberLocationRepository.deleteById(id);
    }
}
