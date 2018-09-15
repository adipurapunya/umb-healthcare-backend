package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umb.cppbt.rekammedik.rekammedik.domain.TransactionNurseList;

public interface TransactionListNurseDbRepository extends JpaRepository<TransactionNurseList, Long> {
	
	@Query("SELECT u FROM TransactionNurseList u WHERE u.idTransaction.id = :value")
	public Page<TransactionNurseList> findNurseListTrxWithPagination(@Param("value")Long value, Pageable page);
	
	@Query("SELECT u.id FROM TransactionNurseList u WHERE u.idTransaction.id = :idTrx AND u.idNurse.id = :idNurse")
	public Long checkNurseList(@Param("idNurse")Long idNurse, @Param("idTrx")Long idTrx);
	
}
