package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umb.cppbt.rekammedik.rekammedik.domain.UserPatient;


public interface UserPatientDbRepository extends JpaRepository<UserPatient, Long>{

	
	public UserPatient findByEmail(String email);
	
	////////////
	
	@Query("select u from UserPatient u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserPatient> findPatientByFullName(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserPatient u where LOWER (u.patientCode) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserPatient> findPatientByPatientCode(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserPatient u where LOWER (u.email) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserPatient> findPatientByEmail(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserPatient u where LOWER (u.phoneNumber) LIKE LOWER( CONCAT('%',:value,'%'))")
	public Page<UserPatient> findPatientByPhoneNumber(@Param("value")String value, Pageable pageable);
	
	@Query("select u from UserPatient u where u.id = :value")
	public Page<UserPatient> findPatientById(@Param("value")Long value, Pageable pageable);
	
	@Query("select u from UserPatient u where u.deviceCode = :value")
	public Page<UserPatient> findPatientByDeviceCode(@Param("value")String value, Pageable pageable);
	
	/////////////
	
	@Query("select u from UserPatient u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserPatient> findPatientByFullNameByIdClinic(@Param("value")String value,@Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserPatient u where LOWER (u.patientCode) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserPatient> findPatientByPatientCodeByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserPatient u where LOWER (u.email) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserPatient> findPatientByEmailByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserPatient u where LOWER (u.phoneNumber) LIKE LOWER( CONCAT('%',:value,'%')) AND u.clinic.id = :idClinic")
	public Page<UserPatient> findPatientByPhoneNumberByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserPatient u where u.id = :value AND u.clinic.id = :idClinic")
	public Page<UserPatient> findPatientByIdByIdClinic(@Param("value")Long value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from UserPatient u where u.deviceCode = :value AND u.clinic.id = :idClinic")
	public Page<UserPatient> findPatientByDeviceCodeByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	/////////////
	
	@Query("select u from UserPatient u where u.clinic.id = :value")
	public Page<UserPatient> findPatientWithPaginationByIdClinic(@Param("value")Long value, Pageable pageable);
	
	@Query("select u from UserPatient u")
	public Page<UserPatient> findPatientWithPagination(Pageable page);
	
	@Query("SELECT u.id FROM UserPatient u WHERE u.deviceCode = :ecgCode")
	public Long getPatientId(@Param("ecgCode")String ecgCode);
	

}
