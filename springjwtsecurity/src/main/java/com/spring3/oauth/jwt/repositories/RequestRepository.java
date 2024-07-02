package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
