package com.spring3.oauth.jwt.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring3.oauth.jwt.models.MemberLocation;
import com.spring3.oauth.jwt.repositories.MemberLocationRepository;
import com.spring3.oauth.jwt.repositories.MemberRepository;

@Service
public class MemberLocationService {
     private final MemberLocationRepository memberLocationRepository;
    

    public MemberLocationService(MemberLocationRepository memberLocationRepository) {
        this.memberLocationRepository = memberLocationRepository;
    }

    public List<MemberLocation> getAllMemberLocations() {
    	
    	
        return memberLocationRepository.findAll();
    }

    public Optional<MemberLocation> getMemberLocationById(Long id) {
        return memberLocationRepository.findById(id);
    }

    public MemberLocation saveMemberLocation(MemberLocation memberLocation) {
        return memberLocationRepository.save(memberLocation);
    }

    public void deleteMemberLocation(Long id) {
        memberLocationRepository.deleteById(id);
    }
}
