package com.asos.demo.repository;

import com.asos.demo.entity.Founder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FounderRepository extends JpaRepository<Founder, Long> {
    Optional<Founder> findByEmail(String email);
    boolean existsByEmail(String email);
}
