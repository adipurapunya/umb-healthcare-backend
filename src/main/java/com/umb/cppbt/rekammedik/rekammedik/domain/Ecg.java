package com.umb.cppbt.rekammedik.rekammedik.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ecg")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ecg implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
	
	@Column(name = "ecg_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ecgDate;
    
    @Size(max = 20)
    @Column(name = "ecg_code")
    private String ecgCode;
    
    @Column(name = "analog")
    private Integer analog;
    
    @Column(name = "order_analog")
    private Integer orderAnalog;
    
    
    
	public Ecg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Ecg(Date ecgDate, @Size(max = 20) String ecgCode, Integer analog,Integer orderAnalog) {
		super();
		this.ecgDate = ecgDate;
		this.ecgCode = ecgCode;
		this.analog = analog;
		this.orderAnalog = orderAnalog;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getEcgDate() {
		return ecgDate;
	}

	public void setEcgDate(Date ecgDate) {
		this.ecgDate = ecgDate;
	}

	public String getEcgCode() {
		return ecgCode;
	}

	public void setEcgCode(String ecgCode) {
		this.ecgCode = ecgCode;
	}

	public Integer getAnalog() {
		return analog;
	}

	public void setAnalog(Integer analog) {
		this.analog = analog;
	}

	public Integer getOrderAnalog() {
		return orderAnalog;
	}

	public void setOrderAnalog(Integer orderAnalog) {
		this.orderAnalog = orderAnalog;
	}

	@Override
	public String toString() {
		return "Ecg [id=" + id + ", ecgDate=" + ecgDate + ", ecgCode="
				+ ecgCode + ", analog=" + analog + ", orderAnalog=" + orderAnalog + "]";
	}
    

}
