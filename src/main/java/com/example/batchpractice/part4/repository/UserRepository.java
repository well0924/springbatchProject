package com.example.batchpractice.part4.repository;

import com.example.batchpractice.part4.domain.BatchMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;

public interface UserRepository extends JpaRepository<BatchMember,Long> {
    Collection<BatchMember> findAllByUpdatedDate(LocalDate updatedDate);
}
