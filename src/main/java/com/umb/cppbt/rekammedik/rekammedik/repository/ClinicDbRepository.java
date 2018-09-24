package com.umb.cppbt.rekammedik.rekammedik.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umb.cppbt.rekammedik.rekammedik.domain.Clinic;

public interface ClinicDbRepository extends JpaRepository<Clinic, Long> {
	
	@Override
	public Page<Clinic> findAll(Pageable arg0);
	
	@Query("select c from Clinic c where LOWER (c.nameOfClinic) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<Clinic> findClinicByName(@Param("value")String value, Pageable pageable);
	
	@Query("select c from Clinic c where LOWER (c.codeOfClinic) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<Clinic> findClinicByClinicCode(@Param("value")String value, Pageable pageable);
	
	@Query("select c from Clinic c where c.statusActive = true")
	public List<Clinic> findAllStatusActive();
}
