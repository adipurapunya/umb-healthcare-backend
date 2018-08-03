package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umb.cppbt.rekammedik.rekammedik.domain.UserClinic;


public interface UserClinicDbRepository extends JpaRepository<UserClinic, Long>{

	public UserClinic findUserClinicByEmail(String email);
	
	////////////
		
	@Query("select u from UserClinic u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserClinic> findUserClinicByFullName(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserClinic u where LOWER (u.userCode) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserClinic> findUserClinicByUserClinicCode(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserClinic u where LOWER (u.email) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserClinic> findUserClinicByEmail(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserClinic u where LOWER (u.phoneNumber) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserClinic> findUserClinicByPhoneNumber(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserClinic u where u.id = :value")
	public Page<UserClinic> findUserClinicById(@Param("value")Long value, Pageable pageable);
	
	/////////////
	
	@Query("select u from UserClinic u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserClinic> findUserClinicByFullNameByIdClinic(@Param("value")String value,@Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserClinic u where LOWER (u.userCode) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserClinic> findUserClinicByUserClinicCodeByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserClinic u where LOWER (u.email) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserClinic> findUserClinicByEmailByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserClinic u where LOWER (u.phoneNumber) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserClinic> findUserClinicByPhoneNumberByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserClinic u where u.id = :value AND u.clinic.id = :idClinic")
	public Page<UserClinic> findUserClinicByIdByIdClinic(@Param("value")Long value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	/////////////
	
	@Query("select u from UserClinic u where u.clinic.id = :value")
	public Page<UserClinic> findUserClinicWithPaginationByIdClinic(@Param("value")Long value, Pageable pageable);
	
	@Query("select u from UserClinic u")
	public Page<UserClinic> findUserClinicWithPagination(Pageable page);
		
}
