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
@Table(name = "services")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Services implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Size(max = 100)
    @Column(name = "name_of_services")
    private String nameOfservices;
    
    @Size(max = 20)
    @Column(name = "code_of_services")
    private String codeOfservices;
    
    @Column(name = "price")
    private Float price;
    
   
	public Services() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Services(@Size(max = 100) String nameOfservices, @Size(max = 20) String codeOfservices, Float price) {
		super();
		this.nameOfservices = nameOfservices;
		this.codeOfservices = codeOfservices;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameOfservices() {
		return nameOfservices;
	}

	public void setNameOfservices(String nameOfservices) {
		this.nameOfservices = nameOfservices;
	}

	public String getCodeOfservices() {
		return codeOfservices;
	}

	public void setCodeOfservices(String codeOfservices) {
		this.codeOfservices = codeOfservices;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	
        
}
