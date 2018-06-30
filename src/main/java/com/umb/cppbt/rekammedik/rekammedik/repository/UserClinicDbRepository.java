package com.umb.cppbt.rekammedik.rekammedik.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.umb.cppbt.rekammedik.rekammedik.domain.UserClinic;


public interface UserClinicDbRepository extends JpaRepository<UserClinic, Long>{

	public UserClinic findByUsername(String username);
	
	public UserClinic findByEmail(String email);
	
	public UserClinic findByPhoneNumber(String phone);
	
	public UserClinic findByUserCode (String code);
	
	@Query( "select u from UserClinic u" )
	public List<UserClinic> findAllUsers(Pageable pageable);
	
	@Query("select u from UserClinic u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public List<UserClinic> findUserByFullName(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserClinic u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public List<UserClinic> findUserByFullNameGetCount(@Param("value")String value);
	

	
}
