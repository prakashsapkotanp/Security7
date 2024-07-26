package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.models.MemberInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberInfo, Long> {

    List<MemberInfo> findByFirstname(String firstname);

    List<MemberInfo> findByLastname(String lastname);

    List<MemberInfo> findByMiddlename(String middlename);

    List<MemberInfo> findByBloodGroup(String bloodGroup);

    List<MemberInfo> findByGender(String gender);

    List<MemberInfo> findByDateOfBirth(Date dateOfBirth);

    @Query(value = "SELECT m.id,m.blood_group, m.date_of_birth,m.firstname, m.gender, m.last_time_of_donation,m.lastname,m.middlename,m.registration_date,m.location_id, m.user_id  FROM members m JOIN member_location ml ON m.location_id = ml.id WHERE m.blood_group = :bloodGroup AND \n" +
            "            6371 * acos(cos(radians(:latitude)) * cos(radians(ml.latitude)) * cos(radians(:longitude) - radians(80.621591)) + sin(radians(:latitude)) * sin(radians(ml.latitude))) <= :radius", nativeQuery = true)
    List<MemberInfo> findByBloodGroupAndLocation(@Param("bloodGroup") String bloodGroup,
                                                 @Param("latitude") double latitude,
                                                 @Param("longitude") double longitude,
                                                 @Param("radius") double radius);

    @Modifying
    @Transactional
    @Query(value = "UPDATE members set user_id = ?1 where id = ?2", nativeQuery = true)
    public void setUserId(Long userId, Long id);
}
