/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umb.cppbt.rekammedik.rekammedik.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "transaction_nurse_list")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TransactionNurseList implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    	
	@Size(max = 20)
	@Column(name = "day")
    private String day;
	
	@Column(name = "time")
    @Temporal(TemporalType.TIME)
    private Date time;
	
	@Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
	
	@Column(name = "rate_status")
    private Boolean rateStatus;
	
	@Column(name = "acceptance_status")
    private Boolean acceptanceStatus;
	
	@Size(max = 100)
	@Column(name = "reason_acceptance_status")
    private String reasonAcceptanceStatus;
	
	@Size(max = 200)
	@Column(name = "treatement")
    private String treatement;
	
	@JoinColumn(name = "nurse_id", referencedColumnName = "id")
    @ManyToOne
    private UserNurse idNurse;
	
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @ManyToOne
    private Transaction idTransaction;

	public TransactionNurseList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getRateStatus() {
		return rateStatus;
	}

	public void setRateStatus(Boolean rateStatus) {
		this.rateStatus = rateStatus;
	}

	public Boolean getAcceptanceStatus() {
		return acceptanceStatus;
	}

	public void setAcceptanceStatus(Boolean acceptanceStatus) {
		this.acceptanceStatus = acceptanceStatus;
	}

	public String getReasonAcceptanceStatus() {
		return reasonAcceptanceStatus;
	}

	public void setReasonAcceptanceStatus(String reasonAcceptanceStatus) {
		this.reasonAcceptanceStatus = reasonAcceptanceStatus;
	}

	public String getTreatement() {
		return treatement;
	}

	public void setTreatement(String treatement) {
		this.treatement = treatement;
	}

	public UserNurse getIdNurse() {
		return idNurse;
	}

	public void setIdNurse(UserNurse idNurse) {
		this.idNurse = idNurse;
	}

	public Transaction getIdTransaction() {
		return idTransaction;
	}

	public void setIdTransaction(Transaction idTransaction) {
		this.idTransaction = idTransaction;
	}
	
   
}
