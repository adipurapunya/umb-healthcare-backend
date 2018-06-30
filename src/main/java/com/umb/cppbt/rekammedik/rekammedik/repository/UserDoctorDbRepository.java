package com.umb.cppbt.rekammedik.rekammedik.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.umb.cppbt.rekammedik.rekammedik.domain.UserDoctor;


public interface UserDoctorDbRepository extends JpaRepository<UserDoctor, Long>{

	public UserDoctor findByUsername(String username);
	
	public UserDoctor findByEmail(String email);
	
	public UserDoctor findByPhoneNumber(String phone);
	
	public UserDoctor findByDoctorCode (String code);
	
	@Query( "select u from UserDoctor u" )
	public List<UserDoctor> findAllUsers(Pageable pageable);
	
	@Query("select u from UserDoctor u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public List<UserDoctor> findUserByFullName(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserDoctor u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public List<UserDoctor> findUserByFullNameGetCount(@Param("value")String value);
	

	
}
