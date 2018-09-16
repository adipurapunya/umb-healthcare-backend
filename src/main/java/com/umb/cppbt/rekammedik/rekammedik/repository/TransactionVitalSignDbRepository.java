package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.umb.cppbt.rekammedik.rekammedik.domain.TransactionVitalSign;

public interface TransactionVitalSignDbRepository extends JpaRepository<TransactionVitalSign, Long> {

	@Query("SELECT u FROM TransactionVitalSign u WHERE u.idTransaction.id = :idTrx")
	public TransactionVitalSign findVitalSignByIdTransaction(@Param("idTrx")Long idTrx);
	
}
