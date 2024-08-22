package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.models.DonorInfo;
import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.models.Request;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester(MemberInfo requesterInfo);
    List<Request> findByDonorInfo(Optional<DonorInfo> donorInfo);
    List<Request> findByRequesterId(Long requesterId);
    @Transactional
    @Modifying
    @Query(value = "update tbl_request set  total_pints_donated = total_pints_donated -1 where requester_id = ?1 and total_pints_donated>=1",nativeQuery = true)
    void updatePints(Long requesterId);
    @Transactional
    @Modifying
    @Query(value = "insert into tbl_request (current_latitude,current_longitude,disabled,total_pints_donated,donor_id,requester_id,created_at) values (?4,?5,1,?3,?2,?1,now(6))",nativeQuery = true)
    public void insert(Long requesterId,Long donorId,int pints, double lat, double lng);
    @Query(value = "select * from tbl_request", nativeQuery = true)
    public List<Request> manualFindAll();

    @Transactional
    @Modifying
    @Query(value = "UPDATE `db_bloodlink`.`tbl_request` SET `disabled` = b'0', total_pints_donated = total_pints_donated - 1 WHERE id =?1 and total_pints_donated >0",nativeQuery = true)
    public void updateRequestTable(Long reqId,Long userId);
    @Transactional
    @Modifying
    @Query(value = "UPDATE `db_bloodlink`.`tbl_request` SET  total_pints_donated = total_pints_donated - 1 WHERE id =?1 and total_pints_donated >0",nativeQuery = true)
    public void reducePints(Long reqId,Long userId);
//    @Query(value ="UPDATE `db_bloodlink`.`tbl_request` SET `disabled` = b'0' WHERE id = ?1")
//    public List

}
