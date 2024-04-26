package com.spring3.oauth.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring3.oauth.jwt.models.MemberLocation;

public interface MemberLocationRepository extends JpaRepository<MemberLocation,Long>{
    
    
}
