package com.umb.cppbt.rekammedik.rekammedik.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.umb.cppbt.rekammedik.rekammedik.domain.UserNurse;


public interface UserNurseDbRepository extends JpaRepository<UserNurse, Long>{

	public UserNurse findByUsername(String username);
	
	public UserNurse findByEmail(String email);
	
	public UserNurse findByPhoneNumber(String phone);
	
	public UserNurse findByNurseCode (String code);
	
	@Query( "select u from UserNurse u" )
	public List<UserNurse> findAllUsers(Pageable pageable);
	
	@Query("select u from UserNurse u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public List<UserNurse> findUserByFullName(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserNurse u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public List<UserNurse> findUserByFullNameGetCount(@Param("value")String value);
	

	
}
