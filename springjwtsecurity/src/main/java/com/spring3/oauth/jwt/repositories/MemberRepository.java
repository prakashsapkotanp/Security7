package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.models.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query(value = "SELECT m.id,m.blood_group, m.date_of_birth,m.firstname, m.gender, m.last_time_of_donation,m.lastname,m.middlename,m.registration_date,m.location_id, m.user_id   FROM MemberInfo m JOIN m.memberLocation ml WHERE m.bloodGroup = :bloodGroup AND " +
            "SQRT(POWER(ml.latitude - :latitude, 2) + POWER(ml.longitude - :longitude, 2)) <= :radius", nativeQuery = true)
    List<MemberInfo> findByBloodGroupAndLocation(@Param("bloodGroup") String bloodGroup,
                                                 @Param("latitude") double latitude,
                                                 @Param("longitude") double longitude,
                                                 @Param("radius") double radius);


}
