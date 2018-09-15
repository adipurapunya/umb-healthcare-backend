package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umb.cppbt.rekammedik.rekammedik.domain.TransactionServiceList;

public interface TransactionListServicesDbRepository extends JpaRepository<TransactionServiceList, Long> {
	
	@Query("select u from TransactionServiceList u where u.idTransaction.id = :value")
	public Page<TransactionServiceList> findServicesListTrxWithPagination(@Param("value")Long value, Pageable page);
}
