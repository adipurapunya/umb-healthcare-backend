package com.umb.cppbt.rekammedik.rekammedik.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.umb.cppbt.rekammedik.rekammedik.domain.UserPatient;


public interface UserPatientDbRepository extends JpaRepository<UserPatient, Long>{

	public UserPatient findByUsername(String username);
	
	public UserPatient findByEmail(String email);
	
	public UserPatient findByPhoneNumber(String phone);
	
	public UserPatient findByPatientCode (String code);
	
	@Query( "select u from UserPatient u" )
	public List<UserPatient> findAllUsers(Pageable pageable);
	
	@Query("select u from UserPatient u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public List<UserPatient> findUserByFullName(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserPatient u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public List<UserPatient> findUserByFullNameGetCount(@Param("value")String value);
	

	
}
