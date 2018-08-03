package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.umb.cppbt.rekammedik.rekammedik.domain.UserDoctor;


public interface UserDoctorDbRepository extends JpaRepository<UserDoctor, Long>{

	public UserDoctor findDoctorByEmail(String email);
	
	////////////
		
	@Query("select u from UserDoctor u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserDoctor> findDoctorByFullName(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserDoctor u where LOWER (u.doctorCode) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserDoctor> findDoctorByDoctorCode(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserDoctor u where LOWER (u.email) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserDoctor> findDoctorByEmail(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserDoctor u where LOWER (u.phoneNumber) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserDoctor> findDoctorByPhoneNumber(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserDoctor u where u.id = :value")
	public Page<UserDoctor> findDoctorById(@Param("value")Long value, Pageable pageable);
	
	@Query("select u from UserDoctor u where u.registerNumber = :value")
	public Page<UserDoctor> findDoctorByRegisterNumber(@Param("value")String value, Pageable pageable);
	
	/////////////
	
	@Query("select u from UserDoctor u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserDoctor> findDoctorByFullNameByIdClinic(@Param("value")String value,@Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserDoctor u where LOWER (u.doctorCode) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserDoctor> findDoctorByDoctorCodeByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserDoctor u where LOWER (u.email) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserDoctor> findDoctorByEmailByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserDoctor u where LOWER (u.phoneNumber) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserDoctor> findDoctorByPhoneNumberByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserDoctor u where u.id = :value AND u.clinic.id = :idClinic")
	public Page<UserDoctor> findDoctorByIdByIdClinic(@Param("value")Long value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserDoctor u where u.registerNumber = :value AND u.clinic.id = :idClinic")
	public Page<UserDoctor> findDoctorByRegisterNumberByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	/////////////
	
	@Query("select u from UserDoctor u where u.clinic.id = :value")
	public Page<UserDoctor> findDoctorWithPaginationByIdClinic(@Param("value")Long value, Pageable pageable);
	
	@Query("select u from UserDoctor u")
	public Page<UserDoctor> findDoctorWithPagination(Pageable page);
	
	
}
