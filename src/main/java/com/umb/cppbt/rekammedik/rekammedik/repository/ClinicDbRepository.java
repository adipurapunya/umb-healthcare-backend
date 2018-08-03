package com.umb.cppbt.rekammedik.rekammedik.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umb.cppbt.rekammedik.rekammedik.domain.Clinic;

public interface ClinicDbRepository extends JpaRepository<Clinic, Long> {
	
}
