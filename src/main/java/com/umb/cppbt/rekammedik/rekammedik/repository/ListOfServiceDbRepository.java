package com.umb.cppbt.rekammedik.rekammedik.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.umb.cppbt.rekammedik.rekammedik.domain.ListOfServices;
import com.umb.cppbt.rekammedik.rekammedik.domain.Services;

public interface ListOfServiceDbRepository extends JpaRepository<ListOfServices, Long> {
	
	@Query("SELECT L.services FROM ListOfServices L WHERE L.clinic.id = :idClinic")
	public List<Services> findByClinicIdWithCustomQuery(@Param("idClinic")Long idClinic);
	
	public List<ListOfServices> findByClinicId(Long id);
	
	
}
