package com.umb.cppbt.rekammedik.rekammedik.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.umb.cppbt.rekammedik.rekammedik.domain.UserAdmin;

public interface UserAdminDbRepository extends JpaRepository<UserAdmin, Long>{

	public UserAdmin findByUsername(String username);
	
	public UserAdmin findByEmail(String email);
	
	public UserAdmin findByPhoneNumber(String phone);
	
	public UserAdmin findByAdminCode (String code);
	
	@Query( "select u from UserAdmin u" )
	public List<UserAdmin> findAllUsers(Pageable pageable);
	
	@Query("select u from UserAdmin u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public List<UserAdmin> findUserByFullName(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserAdmin u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public List<UserAdmin> findUserByFullNameGetCount(@Param("value")String value);
	

	
}
