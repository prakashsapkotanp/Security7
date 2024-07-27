package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.models.Request;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester(MemberInfo requesterInfo);
    List<Request> findByDonorInfo(MemberInfo donorInfo);
    List<Request> findByRequesterId(Long requesterId);
    @Transactional
    @Modifying
    @Query(value = "insert into tbl_request (current_latitude,current_longitude,disabled,total_pints_donated,donor_id,requester_id) values (?4,?5,1,?3,?2,?1)",nativeQuery = true)
    public void insert(Long requesterId,Long donorId,int pints, double lat, double lng);
    @Query(value = "select * from tbl_request", nativeQuery = true)
    public List<Request> manualFindAll();

}
