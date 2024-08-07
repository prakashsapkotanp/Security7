package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester(MemberInfo requesterInfo);
    List<Request> findByDonorInfo(MemberInfo donorInfo);
    List<Request> findByRequesterId(Long requesterId);
}
