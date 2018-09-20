package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.umb.cppbt.rekammedik.rekammedik.domain.Clinic;

public interface ClinicDbRepository extends JpaRepository<Clinic, Long> {
	
}
