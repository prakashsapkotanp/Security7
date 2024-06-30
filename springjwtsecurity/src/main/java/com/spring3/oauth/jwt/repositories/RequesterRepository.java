package com.spring3.oauth.jwt.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring3.oauth.jwt.models.RequesterInfo;

@Repository
public interface RequesterRepository extends JpaRepository<RequesterInfo,Long> {
    Optional<RequesterInfo> findById(Long id);

    List<RequesterInfo> findByBloodGroup(String bloodGroup);

    List<RequesterInfo> findByLatitudeAndLongitude(double latitude, double longitude);

    List<RequesterInfo> findByName(String name);
}
