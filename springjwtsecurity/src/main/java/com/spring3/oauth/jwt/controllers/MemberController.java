package com.spring3.oauth.jwt.controllers;

import java.sql.Date;
import java.util.List;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring3.oauth.jwt.models.MemberInfo;
import com.spring3.oauth.jwt.services.MemberService;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping
    public List<MemberInfo> getAllMembers() {
        return memberService.getAllMembers();
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
        } else if (email != null) {
            return memberService.searchByEmail(email);
        } else if (bloodGroup != null) {
            return memberService.searchByBloodGroup(bloodGroup);
        } else if (gender != null) {
            return memberService.searchByGender(gender);
        } else if (dateOfBirth != null) {
            return memberService.searchByDateOfBirth(dateOfBirth);
        } else {
            // Handle invalid or missing parameters
            if (firstname != null ||
                lastname != null ||
                middlename != null ||
                email != null ||
                bloodGroup != null ||
                gender != null||
                dateOfBirth !=null
                ) 
                {
                
                    throw new IllegalArgumentException("No member found");
                }
            else{
                throw new IllegalArgumentException("At least one search parameter is required");
                }
        }
    }

    @PostMapping
    public MemberInfo saveMember(@RequestBody MemberInfo memberInfo) {
        return memberService.saveMember(memberInfo);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
    }
}
