/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umb.cppbt.rekammedik.rekammedik.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "transaction")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Transaction implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
	@Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
	
	@Column(name = "date_order_in")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOrderIn;
	
	@Column(name = "date_treatement_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTreatementStart;
	
	@Column(name = "date_treatement_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTreatementEnd;
    
	@Column(name = "fixed_price")
    private Float fixedPrice;
    
	@Size(max = 75)
	@Column(name = "prediction_price")
    private String predictionPrice;
    
	@Column(name = "prepaid_price")
    private Float prepaidPrice;
	
	@Column(name = "refund_price")
    private Float refundPrice;
	
	@Column(name = "sum_of_days")
    private Integer sumOfDays;
	
	@Column(name = "expired_transaction_time_fixed_price")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredTransactionTimeFixedPrice;
	
	@Column(name = "expired_transaction_time_prepaid_price")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredTransactionTimePrepaidPrice;
    
	@Size(max = 255)
    @Column(name = "receipt_path")
    private String receiptPath;
    
	@Column(name = "location_latitude")
    private Float locationLatitude;
    
	@Column(name = "location_longitude")
    private Float locationLongitude;
    
	@Size(max = 255)
    @Column(name = "transaction_description")
    private String transactionDescription;
	
	@Size(max = 255)
    @Column(name = "address_to_visit")
    private String addressToVisit;
	 
	@Size(max = 50)
	@Column(name = "order_number")
    private String orderNumber;
	
	@Size(max = 255)
	@Column(name = "diagnosis")
    private String diagnosis;
	
	@Size(max = 255)
	@Column(name = "medicine")
    private String medicine;
	
	@Size(max = 255)
	@Column(name = "nurse_action")
    private String nurseAction;
    
	@OneToMany(mappedBy = "idTransaction", cascade= CascadeType.REMOVE)
    private List<TransactionNurseList> nurseList;
	
	@OneToMany(mappedBy = "idTransaction", cascade= CascadeType.REMOVE)
    private List<TransactionDoctorList> doctorList;
	
	@OneToMany(mappedBy = "idTransaction",  cascade= CascadeType.REMOVE)
    private List<TransactionServiceList> serviceList;
    
	@JoinColumn(name = "transaction_type_id", referencedColumnName = "id")
    @ManyToOne
    private SystemTransactionType transactionTypeId;
    
	@JoinColumn(name = "transaction_status_id", referencedColumnName = "id")
    @ManyToOne
    private SystemTransactionStatus transactionStatusId;
	
	@JoinColumn(name = "payment_PrepaidPrice_status_id", referencedColumnName = "id")
    @ManyToOne
    private SystemPaymentStatus paymentPrepaidPriceStatusId;
	
	@JoinColumn(name = "payment_FixedPrice_status_id", referencedColumnName = "id")
    @ManyToOne
    private SystemPaymentStatus paymentFixedPriceStatusId;
    
	@JoinColumn(name = "patient_id", referencedColumnName = "id")
    @ManyToOne
    private UserPatient userPatient;
    
	@JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    @ManyToOne
    private PaymentMethod paymentMethodId;
	
	@JoinColumn(name = "clinic_id", referencedColumnName = "id")
    @ManyToOne
    private Clinic idClinic;

	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateOrderIn() {
		return dateOrderIn;
	}

	public void setDateOrderIn(Date dateOrderIn) {
		this.dateOrderIn = dateOrderIn;
	}

	public Date getDateTreatementStart() {
		return dateTreatementStart;
	}

	public void setDateTreatementStart(Date dateTreatementStart) {
		this.dateTreatementStart = dateTreatementStart;
	}

	public Date getDateTreatementEnd() {
		return dateTreatementEnd;
	}

	public void setDateTreatementEnd(Date dateTreatementEnd) {
		this.dateTreatementEnd = dateTreatementEnd;
	}

	public Float getFixedPrice() {
		return fixedPrice;
	}

	public void setFixedPrice(Float fixedPrice) {
		this.fixedPrice = fixedPrice;
	}

	public String getPredictionPrice() {
		return predictionPrice;
	}

	public void setPredictionPrice(String predictionPrice) {
		this.predictionPrice = predictionPrice;
	}

	public Float getPrepaidPrice() {
		return prepaidPrice;
	}

	public void setPrepaidPrice(Float prepaidPrice) {
		this.prepaidPrice = prepaidPrice;
	}

	public Float getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Float refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Integer getSumOfDays() {
		return sumOfDays;
	}

	public void setSumOfDays(Integer sumOfDays) {
		this.sumOfDays = sumOfDays;
	}

	public Date getExpiredTransactionTimeFixedPrice() {
		return expiredTransactionTimeFixedPrice;
	}

	public void setExpiredTransactionTimeFixedPrice(
			Date expiredTransactionTimeFixedPrice) {
		this.expiredTransactionTimeFixedPrice = expiredTransactionTimeFixedPrice;
	}

	public Date getExpiredTransactionTimePrepaidPrice() {
		return expiredTransactionTimePrepaidPrice;
	}

	public void setExpiredTransactionTimePrepaidPrice(
			Date expiredTransactionTimePrepaidPrice) {
		this.expiredTransactionTimePrepaidPrice = expiredTransactionTimePrepaidPrice;
	}

	public String getReceiptPath() {
		return receiptPath;
	}

	public void setReceiptPath(String receiptPath) {
		this.receiptPath = receiptPath;
	}

	public Float getLocationLatitude() {
		return locationLatitude;
	}

	public void setLocationLatitude(Float locationLatitude) {
		this.locationLatitude = locationLatitude;
	}

	public Float getLocationLongitude() {
		return locationLongitude;
	}

	public void setLocationLongitude(Float locationLongitude) {
		this.locationLongitude = locationLongitude;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getAddressToVisit() {
		return addressToVisit;
	}

	public void setAddressToVisit(String addressToVisit) {
		this.addressToVisit = addressToVisit;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@XmlTransient
	@JsonIgnore
	public List<TransactionNurseList> getNurseList() {
		return nurseList;
	}

	public void setNurseList(List<TransactionNurseList> nurseList) {
		this.nurseList = nurseList;
	}

	@XmlTransient
	@JsonIgnore
	public List<TransactionDoctorList> getDoctorList() {
		return doctorList;
	}

	public void setDoctorList(List<TransactionDoctorList> doctorList) {
		this.doctorList = doctorList;
	}

	public List<TransactionServiceList> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<TransactionServiceList> serviceList) {
		this.serviceList = serviceList;
	}

	public SystemTransactionType getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(SystemTransactionType transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public SystemTransactionStatus getTransactionStatusId() {
		return transactionStatusId;
	}

	public void setTransactionStatusId(SystemTransactionStatus transactionStatusId) {
		this.transactionStatusId = transactionStatusId;
	}

	public SystemPaymentStatus getPaymentPrepaidPriceStatusId() {
		return paymentPrepaidPriceStatusId;
	}

	public void setPaymentPrepaidPriceStatusId(
			SystemPaymentStatus paymentPrepaidPriceStatusId) {
		this.paymentPrepaidPriceStatusId = paymentPrepaidPriceStatusId;
	}

	public SystemPaymentStatus getPaymentFixedPriceStatusId() {
		return paymentFixedPriceStatusId;
	}

	public void setPaymentFixedPriceStatusId(
			SystemPaymentStatus paymentFixedPriceStatusId) {
		this.paymentFixedPriceStatusId = paymentFixedPriceStatusId;
	}

	public UserPatient getUserPatient() {
		return userPatient;
	}

	public void setUserPatient(UserPatient userPatient) {
		this.userPatient = userPatient;
	}

	public PaymentMethod getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(PaymentMethod paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public Clinic getIdClinic() {
		return idClinic;
	}

	public void setIdClinic(Clinic idClinic) {
		this.idClinic = idClinic;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getMedicine() {
		return medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}

	public String getNurseAction() {
		return nurseAction;
	}

	public void setNurseAction(String nurseAction) {
		this.nurseAction = nurseAction;
	}
	
	
}
