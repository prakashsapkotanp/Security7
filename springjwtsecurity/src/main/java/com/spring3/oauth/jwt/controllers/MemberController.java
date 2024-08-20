package com.spring3.oauth.jwt.controllers;

import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.repositories.MemberRepository;
import com.spring3.oauth.jwt.services.MemberService;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public MemberController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public List<MemberInfo> getAllMembers() {
        System.out.println(memberService.getAllMembers());
        return memberService.getAllMembers();
    }
    @PostMapping("setUserId/{memberId}/{userId}")
    public String setUserId(@PathVariable("memberId") Long memberId, @PathVariable("userId") Long userId) {
        memberRepository.setUserId(userId,memberId);
        return "success";
    }



    @GetMapping("/{id}")
    public MemberInfo getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    @GetMapping("/search")
    public List<MemberInfo> searchMembers(
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String middlename,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Date dateOfBirth) {

        if (firstname != null) {
            return memberService.searchByFirstName(firstname);
        } else if (lastname != null) {
            return memberService.searchByLastName(lastname);
        } else if (middlename != null) {
            return memberService.searchByMiddleName(middlename);
        } else if (bloodGroup != null) {
            return memberService.searchByBloodGroup(bloodGroup);
        } else if (gender != null) {
            return memberService.searchByGender(gender);
        } else if (dateOfBirth != null) {
            return memberService.searchByDateOfBirth(dateOfBirth);
        } else {
            throw new IllegalArgumentException("At least one search parameter is required");
        }
    }

    @GetMapping("/potential-donors")
    public List<MemberInfo> getPotentialDonors(@RequestParam String bloodGroup,
                                               @RequestParam double latitude,
                                               @RequestParam double longitude,
                                               @RequestParam double radius) {
        return memberService.findPotentialDonors(bloodGroup, latitude, longitude, radius);
    }

    @PostMapping
    public MemberInfo saveMember(@RequestBody MemberInfo memberInfo) {

        return memberService.saveMember(memberInfo);
    }
    @PostMapping("/updateLastDonatedDate/{memberId}")
    public String updateLastDonatedDate(@PathVariable Long memberId) {
        Optional<MemberInfo> memberInfo = memberRepository.findById(memberId);
        memberInfo.ifPresent(memberInfo1 -> {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            memberInfo1.setLastTimeOfDonation(Date.valueOf(LocalDate.now()));
            memberRepository.save(memberInfo1);
        });
        return "successfully date updated";
    }

    @PutMapping("/{id}")
    public MemberInfo updateMember(@PathVariable Long id, @RequestBody MemberInfo memberInfo) {
        return memberService.updateMember(id, memberInfo);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
    }
}