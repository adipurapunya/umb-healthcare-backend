package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umb.cppbt.rekammedik.rekammedik.domain.Services;

public interface ServiceDbRepository extends JpaRepository<Services, Long> {

}
