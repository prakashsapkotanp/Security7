package com.spring3.oauth.jwt.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring3.oauth.jwt.models.DonorInfo;

public interface DonorRepository extends JpaRepository<DonorInfo,Long>{
    
    //search donor information by memberID
    Optional<DonorInfo> findByMemberInfoId(Long memberId);
    
}
