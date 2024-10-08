package com.develop.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.develop.accountservice.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {}
