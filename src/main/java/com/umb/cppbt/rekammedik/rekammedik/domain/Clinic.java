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
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "clinic")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Clinic implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Size(max = 100)
    @Column(name = "name_of_clinic")
    private String nameOfClinic;
    
    @Size(max = 20)
    @Column(name = "code_of_clinic")
    private String codeOfClinic;
    
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    
    @Size(max = 20)
    @Column(name = "status")
    private String status;
    
    @Column(name = "status_active")
    private Boolean statusActive;
    
    @Size(max = 20)
    @Column(name = "challenge_code")
    private String challengeCode;

	public Clinic() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Clinic(@Size(max = 100) String nameOfClinic,
			@Size(max = 20) String codeOfClinic,
			@Size(max = 255) String address, 
			@Size(max = 20) String status, 
			Boolean statusActive,
			@Size(max = 20) String challengeCode) {
		super();
		this.nameOfClinic = nameOfClinic;
		this.codeOfClinic = codeOfClinic;
		this.address = address;
		this.status = status;
		this.statusActive = statusActive;
		this.challengeCode = challengeCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameOfClinic() {
		return nameOfClinic;
	}

	public void setNameOfClinic(String nameOfClinic) {
		this.nameOfClinic = nameOfClinic;
	}

	public String getCodeOfClinic() {
		return codeOfClinic;
	}

	public void setCodeOfClinic(String codeOfClinic) {
		this.codeOfClinic = codeOfClinic;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getStatusActive() {
		return statusActive;
	}

	public void setStatusActive(Boolean statusActive) {
		this.statusActive = statusActive;
	}

	public String getChallengeCode() {
		return challengeCode;
	}

	public void setChallengeCode(String challengeCode) {
		this.challengeCode = challengeCode;
	}
	
}
