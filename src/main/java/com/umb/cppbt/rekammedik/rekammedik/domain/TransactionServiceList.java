/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umb.cppbt.rekammedik.rekammedik.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "service_list_forTrx")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TransactionServiceList implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    	
	@JoinColumn(name = "service_id", referencedColumnName = "id")
    @ManyToOne
    private Services services;
	
	@JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @ManyToOne
    private Transaction idTransaction;
	
	public TransactionServiceList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
	}

	@XmlTransient
	@JsonIgnore
	public Transaction getIdTransaction() {
		return idTransaction;
	}

	public void setIdTransaction(Transaction idTransaction) {
		this.idTransaction = idTransaction;
	}
	
}
