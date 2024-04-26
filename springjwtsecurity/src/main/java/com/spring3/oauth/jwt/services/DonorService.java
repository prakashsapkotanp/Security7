package com.spring3.oauth.jwt.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spring3.oauth.jwt.models.DonorInfo;
import com.spring3.oauth.jwt.repositories.DonorRepository;

@Service
public class DonorService {
    
    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public List<DonorInfo> getAllDonorInfos() {
        return donorRepository.findAll();
    }

    public Optional<DonorInfo> getDonorInfoById(Long id) {
        return donorRepository.findById(id);
    }

    public DonorInfo saveDonorInfo(DonorInfo donorInfo) {
        return donorRepository.save(donorInfo);
    }

    public void deleteDonorInfo(Long id) {
        donorRepository.deleteById(id);
    }

    //  this method search donor by memberID
    public Optional<DonorInfo> getDonorInfoByMemberId(Long memberId) {
        return donorRepository.findByMemberInfoId(memberId);
    }
    
}
