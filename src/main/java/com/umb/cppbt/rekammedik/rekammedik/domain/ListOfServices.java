package com.umb.cppbt.rekammedik.rekammedik.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "list_of_services")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ListOfServices implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
	@JoinColumn(name = "clinic_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Clinic clinic;
	
	@JoinColumn(name = "service_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Services services;
	
	public ListOfServices() {
		super();
	}
	
	
	public ListOfServices(Clinic clinic, Services services) {
		super();
		this.clinic = clinic;
		this.services = services;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Clinic getClinic() {
		return clinic;
	}

	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
	}
	
	
}
