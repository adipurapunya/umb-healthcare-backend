package com.umb.cppbt.rekammedik.rekammedik.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.umb.cppbt.rekammedik.rekammedik.domain.ListOfServices;
import com.umb.cppbt.rekammedik.rekammedik.domain.Services;

public interface ListOfServiceDbRepository extends JpaRepository<ListOfServices, Long> {
	
	@Query("SELECT L.services FROM ListOfServices L WHERE L.clinic.id = :idClinic")
	public List<Services> findByClinicIdWithCustomQuery(@Param("idClinic")Long idClinic);
	
	@Query("SELECT L.services FROM ListOfServices L WHERE L.clinic.id = :idClinic")
	public Page<Services> findByClinicIdWithCustomQueryWithPagination(@Param("idClinic")Long idClinic, Pageable pageable);
	
	
	@Query("SELECT L FROM ListOfServices L")
	public Page<ListOfServices> findByClinicIdWithCustomQueryWithPaginationForAdmin(Pageable pageable);
	
	//@Query("select u from UserClinic u where LOWER (u.fullName) LIKE LOWER( CONCAT('%',:value,'%'))")
	@Query("SELECT L.services FROM ListOfServices L WHERE L.clinic.id = :idClinic AND LOWER (L.services.nameOfservices) LIKE LOWER(CONCAT('%',:name,'%'))")
	public Page<Services> findByClinicIdWithCustomQueryWithPaginationByName(@Param("idClinic")Long idClinic, @Param("name")String name, Pageable pageable);
	
	
	@Query("SELECT L.services FROM ListOfServices L WHERE L.clinic.id = :idClinic AND LOWER (L.services.codeOfservices) LIKE LOWER(CONCAT('%',:code,'%'))")
	public Page<Services> findByClinicIdWithCustomQueryWithPaginationByCode(@Param("idClinic")Long idClinic, @Param("code")String code, Pageable pageable);
	
	
	@Query("SELECT L.services FROM ListOfServices L WHERE L.clinic.id = :idClinic AND L.services.price = :price")
	public Page<Services> findByClinicIdWithCustomQueryWithPaginationByPrice(@Param("idClinic")Long idClinic, @Param("price")Float price, Pageable pageable);
	
	
	@Query("SELECT L FROM ListOfServices L WHERE L.services.price = :price")
	public Page<ListOfServices> findByClinicIdWithCustomQueryWithPaginationByPriceForAdmin(@Param("price")Float price, Pageable pageable);
	
	@Query("SELECT L FROM ListOfServices L WHERE LOWER (L.services.nameOfservices) LIKE LOWER(CONCAT('%',:name,'%'))")
	public Page<ListOfServices> findByClinicIdWithCustomQueryWithPaginationByNameForAdmin(@Param("name")String name, Pageable pageable);
	
	@Query("SELECT L FROM ListOfServices L WHERE LOWER (L.services.codeOfservices) LIKE LOWER(CONCAT('%',:code,'%'))")
	public Page<ListOfServices> findByClinicIdWithCustomQueryWithPaginationByCodeForAdmin(@Param("code")String code, Pageable pageable);
	
	
	
	
	public List<ListOfServices> findByClinicId(Long id);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM ListOfServices L where L.clinic.id = :clinicId AND L.services.id = :serviceId")
	void deleteListOfServicesByIdClinicByIdService(@Param("clinicId")Long clinicId, @Param("serviceId")Long serviceId);
	
	
}
