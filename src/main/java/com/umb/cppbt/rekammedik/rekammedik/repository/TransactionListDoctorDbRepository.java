package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umb.cppbt.rekammedik.rekammedik.domain.TransactionDoctorList;

public interface TransactionListDoctorDbRepository extends JpaRepository<TransactionDoctorList, Long> {
	
	@Query("select u from TransactionDoctorList u where u.idTransaction.id = :value")
	public Page<TransactionDoctorList> findDoctorListTrxWithPagination(@Param("value")Long value, Pageable page);
	
	@Query("SELECT u.id FROM TransactionDoctorList u WHERE u.idTransaction.id = :idTrx AND u.idDoctor.id = :idDoctor")
	public Long checkDoctorList(@Param("idDoctor")Long idDoctor, @Param("idTrx")Long idTrx);

}
