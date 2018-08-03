package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umb.cppbt.rekammedik.rekammedik.domain.UserNurse;

public interface UserNurseDbRepository extends JpaRepository<UserNurse, Long>{

	public UserNurse findNurseByEmail(String email);
	
	////////////
		
	@Query("select u from UserNurse u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserNurse> findNurseByFullName(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserNurse u where LOWER (u.nurseCode) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserNurse> findNurseByNurseCode(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserNurse u where LOWER (u.email) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserNurse> findNurseByEmail(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserNurse u where LOWER (u.phoneNumber) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserNurse> findNurseByPhoneNumber(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserNurse u where u.id = :value")
	public Page<UserNurse> findNurseById(@Param("value")Long value, Pageable pageable);
	
	@Query("select u from UserNurse u where u.sipp = :value")
	public Page<UserNurse> findNurseBySipp(@Param("value")String value, Pageable pageable);
	
	/////////////
	
	@Query("select u from UserNurse u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserNurse> findNurseByFullNameByIdClinic(@Param("value")String value,@Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserNurse u where LOWER (u.nurseCode) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserNurse> findNurseByNurseCodeByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserNurse u where LOWER (u.email) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserNurse> findNurseByEmailByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserNurse u where LOWER (u.phoneNumber) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserNurse> findNurseByPhoneNumberByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserNurse u where u.id = :value AND u.clinic.id = :idClinic")
	public Page<UserNurse> findNurseByIdByIdClinic(@Param("value")Long value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserNurse u where u.sipp = :value AND u.clinic.id = :idClinic")
	public Page<UserNurse> findNurseBySippByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	/////////////
	
	@Query("select u from UserNurse u where u.clinic.id = :value")
	public Page<UserNurse> findNurseWithPaginationByIdClinic(@Param("value")Long value, Pageable pageable);
	
	@Query("select u from UserNurse u")
	public Page<UserNurse> findNurseWithPagination(Pageable page);
		

	
}
