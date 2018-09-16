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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "vital_sign_list_forTrx")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TransactionVitalSign implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
	
	@Column(name = "sistol")
    private Integer sistol;
	
	@Column(name = "diastol")
    private Integer diastol;
	
	@Column(name = "pulse_oximetry")
    private Integer pulseOximetry;
	
	@Column(name = "heart_beat")
    private Integer heartBeat;
	
	@Column(name = "temperature")
    private Float temperature;
	
	@Column(name = "respiratory")
    private Integer respiratory;
    	
	@JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @OneToOne
    private Transaction idTransaction;
	
	public TransactionVitalSign() {
		super();
	}
	
	public TransactionVitalSign(Integer sistol, Integer diastol,
			Integer pulseOximetry, Integer heartBeat, Float temperature,
			Integer respiratory, Transaction idTransaction) {
		super();
		this.sistol = sistol;
		this.diastol = diastol;
		this.pulseOximetry = pulseOximetry;
		this.heartBeat = heartBeat;
		this.temperature = temperature;
		this.respiratory = respiratory;
		this.idTransaction = idTransaction;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSistol() {
		return sistol;
	}

	public void setSistol(Integer sistol) {
		this.sistol = sistol;
	}

	public Integer getDiastol() {
		return diastol;
	}

	public void setDiastol(Integer diastol) {
		this.diastol = diastol;
	}

	public Integer getPulseOximetry() {
		return pulseOximetry;
	}

	public void setPulseOximetry(Integer pulseOximetry) {
		this.pulseOximetry = pulseOximetry;
	}

	public Integer getHeartBeat() {
		return heartBeat;
	}

	public void setHeartBeat(Integer heartBeat) {
		this.heartBeat = heartBeat;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	public Integer getRespiratory() {
		return respiratory;
	}

	public void setRespiratory(Integer respiratory) {
		this.respiratory = respiratory;
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
