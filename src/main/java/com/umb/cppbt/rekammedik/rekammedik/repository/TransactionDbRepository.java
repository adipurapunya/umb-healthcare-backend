package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.umb.cppbt.rekammedik.rekammedik.domain.Transaction;

public interface TransactionDbRepository extends JpaRepository<Transaction, Long> {
	
	
	@Query("select u from Transaction u where u.userPatient.id = :value")
	public Page<Transaction> findTransactionWithPaginationByIdPatient(@Param("value")Long value, Pageable pageable);
	
	@Query("select u from Transaction u where u.idClinic.id = :value")
	public Page<Transaction> findTransactionWithPaginationByIdClinic(@Param("value")Long value, Pageable pageable);
	
	@Query("select u from Transaction u")
	public Page<Transaction> findTransactionWithPagination(Pageable page);
	
	
	@Query("select u from Transaction u where u.id = :value")
	public Page<Transaction> findTransactionById(@Param("value")Long value, Pageable pageable);
	
	@Query("select u from Transaction u where u.orderNumber = :value")
	public Page<Transaction> findTransactionByOrderNumber(@Param("value")String value, Pageable pageable);
	
	
	@Query("select u from Transaction u where u.id = :value AND u.idClinic.id = :idClinic")
	public Page<Transaction> findTransactionByIdByIdClinic(@Param("value")Long value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from Transaction u where u.orderNumber = :value AND u.idClinic.id = :idClinic")
	public Page<Transaction> findTransactionByOrderNumberByIdClinic(@Param("value")String value, @Param("idClinic")Long idClinic ,Pageable pageable);
	
	@Query("select u from Transaction u where u.id = :value AND u.userPatient.id = :idPatient")
	public Page<Transaction> findTransactionByIdByIdPatient(@Param("value")Long value, @Param("idPatient")Long idClinic ,Pageable pageable);
	
	@Query("select u from Transaction u where u.orderNumber = :value AND u.userPatient.id = :idPatient")
	public Page<Transaction> findTransactionByOrderNumberByIdPatient(@Param("value")String value, @Param("idPatient")Long idPatient ,Pageable pageable);

}
