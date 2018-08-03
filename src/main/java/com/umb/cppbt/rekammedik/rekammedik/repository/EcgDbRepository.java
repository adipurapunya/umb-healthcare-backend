package com.umb.cppbt.rekammedik.rekammedik.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umb.cppbt.rekammedik.rekammedik.domain.Ecg;

public interface EcgDbRepository extends JpaRepository<Ecg, Long> {
	
	@Query("SELECT e FROM Ecg e WHERE e.ecgCode =:ecgCode AND EXTRACT (day FROM e.ecgDate) = :day AND EXTRACT (month FROM e.ecgDate) = :month AND EXTRACT (year FROM e.ecgDate) = :year")
	public Page<Ecg> findEcgDataWithPaginationByEcgCodeAndDate(@Param("ecgCode")String ecgCode, @Param("day") int dayOfMonth, @Param("month") int month,  @Param("year") int year , Pageable pageable);
	
	
	@Query("SELECT e FROM Ecg e WHERE e.ecgCode = :ecgCode AND  e.ecgDate = :dates")
	public Page<Ecg> findEcgDataWithPaginationByEcgCodeAndDate2(@Param("ecgCode")String ecgCode, @Param("dates") Date dates, Pageable pageable);

}
