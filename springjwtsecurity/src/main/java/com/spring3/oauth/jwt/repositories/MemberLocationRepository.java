package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.models.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring3.oauth.jwt.models.MemberLocation;

import java.util.List;

public interface MemberLocationRepository extends JpaRepository<MemberLocation,Long>{

  }
