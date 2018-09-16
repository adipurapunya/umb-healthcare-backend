package com.umb.cppbt.rekammedik.rekammedik.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import scala.util.Random;

import com.umb.cppbt.rekammedik.rekammedik.domain.OrderRespons;
import com.umb.cppbt.rekammedik.rekammedik.domain.ResponMessage;
import com.umb.cppbt.rekammedik.rekammedik.domain.Services;
import com.umb.cppbt.rekammedik.rekammedik.domain.SystemPaymentStatus;
import com.umb.cppbt.rekammedik.rekammedik.domain.Transaction;
import com.umb.cppbt.rekammedik.rekammedik.domain.TransactionDoctorList;
import com.umb.cppbt.rekammedik.rekammedik.domain.TransactionNurseList;
import com.umb.cppbt.rekammedik.rekammedik.domain.TransactionServiceList;
import com.umb.cppbt.rekammedik.rekammedik.domain.TransactionVitalSign;
import com.umb.cppbt.rekammedik.rekammedik.repository.TransactionDbRepository;
import com.umb.cppbt.rekammedik.rekammedik.repository.TransactionListDoctorDbRepository;
import com.umb.cppbt.rekammedik.rekammedik.repository.TransactionListNurseDbRepository;
import com.umb.cppbt.rekammedik.rekammedik.repository.TransactionListServicesDbRepository;
import com.umb.cppbt.rekammedik.rekammedik.repository.TransactionVitalSignDbRepository;
import com.umb.cppbt.rekammedik.rekammedik.repository.UserClinicDbRepository;


@RestController
@RequestMapping(value = "/api")
public class TransactionController {
	
	public static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
	@Autowired
	private TransactionDbRepository transactionDbRepository;
	
	@Autowired
	private TransactionListServicesDbRepository transactionListServicesDbRepository;
	
	@Autowired
	private TransactionListNurseDbRepository transactionListNurseDbRepository;
	
	@Autowired
	private TransactionListDoctorDbRepository transactionListDoctorDbRepository;
	
	@Autowired
	private TransactionVitalSignDbRepository transactionVitalSignDbRepository;
	
	@Autowired
	private UserClinicDbRepository userClinicDbRepository;
	
	private Pageable createPageRequest(int page, int size, String sort, String field) {
		Sort sorts;
		if(sort.equals("ASC")){
			sorts = Sort.by(field).ascending();
		}
		else{
			sorts = Sort.by(field).descending();
		}
		Pageable pageable = PageRequest.of(page, size, sorts);
		return pageable;
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC')")
	@RequestMapping(value = "/transaction/assignNurse", method = RequestMethod.POST)
	public ResponseEntity<Object> assignNurseToTrx(@RequestBody TransactionNurseList transactionNurseList) 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		Long checkData = transactionListNurseDbRepository.checkNurseList(transactionNurseList.getIdNurse().getId(), transactionNurseList.getIdTransaction().getId());
		
		if(checkData == null){
			transactionListNurseDbRepository.save(transactionNurseList);
			
			String message = "Thank You, the nurse has been assigned to this transaction";
			
			OrderRespons respons = new OrderRespons();
			respons.setMessage(message);
			respons.setDate(dateFormat.format(date));
			respons.setHttpStatus(HttpStatus.CREATED);
			
			return new ResponseEntity<Object>(respons , HttpStatus.CREATED);
		}else{
			String message = "Sorry, these nurse has been assigned to this transaction before. Try another nurse !";
			
			OrderRespons respons = new OrderRespons();
			respons.setMessage(message);
			respons.setDate(dateFormat.format(date));
			respons.setHttpStatus(HttpStatus.FORBIDDEN);
			return new ResponseEntity<Object>(respons , HttpStatus.FORBIDDEN);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC')")
	@RequestMapping(value = "/transaction/assignDoctor", method = RequestMethod.POST)
	public ResponseEntity<Object> assignDoctorToTrx(@RequestBody TransactionDoctorList transactionDoctorList) 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		Long checkData = transactionListDoctorDbRepository.checkDoctorList(transactionDoctorList.getIdDoctor().getId(), transactionDoctorList.getIdTransaction().getId());
		
		if(checkData == null){
			transactionListDoctorDbRepository.save(transactionDoctorList);
			
			String message = "Thank You, the doctor has been assigned to this transaction";
			
			OrderRespons respons = new OrderRespons();
			respons.setMessage(message);
			respons.setDate(dateFormat.format(date));
			respons.setHttpStatus(HttpStatus.CREATED);
			
			return new ResponseEntity<Object>(respons , HttpStatus.CREATED);
		}else{
			String message = "Sorry, these doctor has been assigned to this transaction before. Try another doctor !";
			
			OrderRespons respons = new OrderRespons();
			respons.setMessage(message);
			respons.setDate(dateFormat.format(date));
			respons.setHttpStatus(HttpStatus.FORBIDDEN);
			return new ResponseEntity<Object>(respons , HttpStatus.FORBIDDEN);
		}	
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/transaction/addVitalSign/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> addVitalSignToTrx(@PathVariable(value = "id") Long id, @RequestBody TransactionVitalSign transactionVitalSign) 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		//System.out.println(transactionVitalSign.getIdTransaction() + " "+ transactionVitalSign.getHeartBeat());
		
		TransactionVitalSign data = transactionVitalSignDbRepository.findVitalSignByIdTransaction(id);
		
		if(transactionVitalSign.getDiastol() != null){data.setDiastol(transactionVitalSign.getDiastol());}
		if(transactionVitalSign.getSistol() != null){data.setSistol(transactionVitalSign.getSistol());}
		if(transactionVitalSign.getHeartBeat() != null){data.setHeartBeat(transactionVitalSign.getHeartBeat());}
		if(transactionVitalSign.getPulseOximetry() != null){data.setPulseOximetry(transactionVitalSign.getPulseOximetry());}
		if(transactionVitalSign.getRespiratory() != null){data.setRespiratory(transactionVitalSign.getRespiratory());}
		if(transactionVitalSign.getTemperature() != null){data.setTemperature(transactionVitalSign.getTemperature());}
		if(transactionVitalSign.getIdTransaction() != null){data.setIdTransaction(transactionVitalSign.getIdTransaction());}
		
		transactionVitalSignDbRepository.save(data);
		
		String message = "Succesfully, Vital sign have been added to this transaction with Id "+id;
		
		OrderRespons respons = new OrderRespons();
		respons.setMessage(message);
		respons.setDate(dateFormat.format(date));
		respons.setHttpStatus(HttpStatus.CREATED);
		
		return new ResponseEntity<Object>(respons , HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/transaction/getVitalSign/{id}", method = RequestMethod.GET)
	public TransactionVitalSign getVitalSignFromTrx(@PathVariable Long id) {
		TransactionVitalSign data = transactionVitalSignDbRepository.findVitalSignByIdTransaction(id);
		logger.info("Fetching vital sign from transaction with id "+id);
		return data;
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN', 'NURSE', 'PATIENT', 'CLINIC')")
	@RequestMapping(value = "/transaction", method = RequestMethod.POST)
	public ResponseEntity<Object> createTransactionsHomecare(@RequestBody Transaction transaction) 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		//Date tomorrow = new Date(date.getTime() + (1000 * 60 * 60 * 24));
		
		SystemPaymentStatus payment = new SystemPaymentStatus();
		payment.setId(2);
		
		String orderNo = getOrderNumber();
		
		transaction.setOrderNumber(orderNo);
		transaction.setDateOrderIn(date);
		transaction.setFixedPrice(0f);
		transaction.setPredictionPrice("0");
				
		Transaction trx = transactionDbRepository.save(transaction);
		transactionVitalSignDbRepository.save(new TransactionVitalSign(null,null,null,null,null,null,trx));
		
		for(TransactionServiceList lst: transaction.getServiceList()){
			//System.out.println(lst.getId());
			lst.setIdTransaction(trx);
			Services s = new Services();
			s.setId(lst.getId());
			lst.setServices(s);
			transactionListServicesDbRepository.save(lst);
		}
		
		String message = "Thank You, Your order has been recorded in our system with order number " + orderNo;
		
		OrderRespons respons = new OrderRespons();
		
		respons.setMessage(message);
		respons.setDate(dateFormat.format(date));
		respons.setHttpStatus(HttpStatus.CREATED);
		
		return new ResponseEntity<Object>(respons , HttpStatus.CREATED);
	}
	
	public String getOrderNumber(){
		Random ran = new Random();
		int n = ran.nextInt(1000) + 1;
		String val = String.valueOf(n);
		
		Date d = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("ddMMyy");
		String datee = sf.format(d);
		
		return "UM-"+datee+val;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/transaction/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteTransaction(@PathVariable Long id) {
		transactionDbRepository.deleteById(id);
		logger.info("user transaction sucessfully deleted");
		ResponMessage message = new ResponMessage();
		message.setStatus(HttpStatus.OK);
		message.setMessage("user transaction with id "+id+" sucessfully deleted");
		return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/transaction/{id}", method = RequestMethod.PUT )
	public ResponseEntity<Object> updateTransaction(@PathVariable(value = "id") Long id,@RequestBody Transaction transaction ,  @RequestHeader(value="Authorization") String token) 
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		//if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == false){
		if(id != null){		
			Transaction findFirst = transactionDbRepository.getOne(id);
			String mes = null ;
			if(findFirst == null) {
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.NOT_FOUND);
				message.setMessage("trx with id "+ id + " NOT_FOUND");
				return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.NOT_FOUND);
		    }
			
			if(transaction.getAddressToVisit() != null) findFirst.setAddressToVisit(transaction.getAddressToVisit());
			if(transaction.getDate() != null) findFirst.setDate(transaction.getDate());
			if(transaction.getDateOrderIn() != null) findFirst.setDateOrderIn(transaction.getDateOrderIn());
			if(transaction.getOrderNumber() != null) findFirst.setOrderNumber(transaction.getOrderNumber());
			if(transaction.getUserPatient() != null) findFirst.setUserPatient(transaction.getUserPatient());
			if(transaction.getTransactionStatusId() != null) findFirst.setTransactionStatusId(transaction.getTransactionStatusId());
			if(transaction.getAddressToVisit() != null) findFirst.setAddressToVisit(transaction.getAddressToVisit());
			if(transaction.getTransactionTypeId() != null) findFirst.setTransactionTypeId(transaction.getTransactionTypeId());
			if(transaction.getIdClinic() != null) findFirst.setIdClinic(transaction.getIdClinic());
			if(transaction.getPaymentFixedPriceStatusId() != null) findFirst.setPaymentFixedPriceStatusId(transaction.getPaymentFixedPriceStatusId());
			if(transaction.getFixedPrice() != null) findFirst.setFixedPrice(transaction.getFixedPrice());
			if(transaction.getPredictionPrice() != null) findFirst.setPredictionPrice(transaction.getPredictionPrice());
			if(transaction.getDiagnosis()!= null) findFirst.setDiagnosis(transaction.getDiagnosis());
			if(transaction.getMedicine() != null) findFirst.setMedicine(transaction.getMedicine());
			if(transaction.getNurseAction() != null) findFirst.setNurseAction(transaction.getNurseAction());
			
			mes = "Succesfully Update trx with id "+ findFirst.getId();
			transactionDbRepository.save(findFirst);		
			ResponMessage message = new ResponMessage();
			message.setStatus(HttpStatus.OK);
			message.setMessage(mes);
			return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("user UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("id is null");
			//error.setMessage("user with user id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/transactionWithPagination", method = RequestMethod.GET)
	public Page<Transaction> getAllTransactionWithPagination(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField) {
		Page<Transaction> data = transactionDbRepository.findTransactionWithPagination(createPageRequest(page, size, sort, sortField));
		logger.info("Fetching All transaction with pagination order by " + sortField + " " + sort);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/transactionWithPaginationByIdClinic", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllTransactionWithPaginationByIdClinic(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("clinicId") Integer clinicId, @RequestHeader(value="Authorization") String token) {
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		int idUserClinicFromToken = (Integer) claims.get("id");
		Long idClinicTarget = new Long(clinicId);
		Long idClinicActual = userClinicDbRepository.findIdClinicByIdUserClinic(new Long(idUserClinicFromToken));
		
		if(idClinicActual.equals(idClinicTarget)){
			Page<Transaction> data = transactionDbRepository.findTransactionWithPaginationByIdClinic(idClinicTarget,createPageRequest(page, size, sort, sortField));
			logger.info("Fetching All transaction with pagination order by " + sortField + " " + sort +" by id clinic "+ idClinicTarget);
			return new ResponseEntity<Object>(data , new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("UNAUTHORIZED, You can not see another data outside you clinic !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("UNAUTHORIZED, You can not see another data with id clinic = " + idClinicTarget + ". You can only see data with id clinic = "+idClinicActual + " !");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'PATIENT')")
	@RequestMapping(value = "/transactionWithPaginationByIdPatient", method = RequestMethod.GET)
	public Page<Transaction> getAllTransactionWithPaginationByIdPatient(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("patientId") Integer patientId) {
		Long id = new Long(patientId);
		Page<Transaction> data = transactionDbRepository.findTransactionWithPaginationByIdPatient(id,createPageRequest(page, size, sort, sortField));
		logger.info("Fetching All transaction with pagination order by " + sortField + " " + sort +" by id clinic "+ id);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/transactionWithPaginationByFieldByIdClinic", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllTransactionWithPaginationByFieldByIdClinic(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value, @RequestParam("clinicId") Integer clinicId, @RequestHeader(value="Authorization") String token) {
		
		Page<Transaction> data = null;
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		int idUserClinicFromToken = (Integer) claims.get("id");
		Long idClinicTarget = new Long(clinicId);
		Long idClinicActual = userClinicDbRepository.findIdClinicByIdUserClinic(new Long(idUserClinicFromToken));
		
		if(idClinicActual.equals(idClinicTarget)){
			if(searchField.equals("orderNumber")){
				data = transactionDbRepository.findTransactionByOrderNumberByIdClinic(value, idClinicTarget ,createPageRequest(page, size, sort, sortField));
			}
			else if(searchField.equals("id")){
				Long id = new Long(value);
				data = transactionDbRepository.findTransactionByIdByIdClinic(id, idClinicTarget ,createPageRequest(page, size, sort, sortField));
			}
			logger.info("Fetching transaction with "+ searchField +" order by " + sortField + " " + sort);
			return new ResponseEntity<Object>(data , new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("UNAUTHORIZED, You can not see another data outside you clinic !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("UNAUTHORIZED, You can not see another data with id clinic = " + idClinicTarget + ". You can only see data with id clinic = "+idClinicActual + " !");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC','PATIENT')")
	@RequestMapping(value = "/transactionWithPaginationByFieldByIdPatient", method = RequestMethod.GET)
	public Page<Transaction> getAllTransactionWithPaginationByFieldByIdPatient(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value, @RequestParam("patientId") Integer patientId) {
		
		Page<Transaction> data = null;
		Long idClinic = new Long(patientId);
		if(searchField.equals("orderNumber")){
			data = transactionDbRepository.findTransactionByOrderNumberByIdPatient(value, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("id")){
			Long id = new Long(value);
			data = transactionDbRepository.findTransactionByIdByIdPatient(id, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		logger.info("Fetching transaction with "+ searchField +" order by " + sortField + " " + sort);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/transactionWithPaginationByField", method = RequestMethod.GET)
	public Page<Transaction> getAllTransactionWithPaginationByField(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value) {
	
		Page<Transaction> data = null;
		if(searchField.equals("orderNumber")){
			data = transactionDbRepository.findTransactionByOrderNumber(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("id")){
			Long id = new Long(value);
			data = transactionDbRepository.findTransactionById(id, createPageRequest(page, size, sort, sortField));
		}
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/transaction/getNurseList/{id}", method = RequestMethod.GET)
	public Page<TransactionNurseList> getAllNurseListWithPagination(@PathVariable(value = "id") Long id) {
		
		Page<TransactionNurseList> data = null;
		data = transactionListNurseDbRepository.findNurseListTrxWithPagination(id, createPageRequest(0, 10, "ASC", "id"));
		logger.info("Fetching Nurse List for id trx "+ id);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC')")
	@RequestMapping(value = "/transaction/deleteNurseList", method = RequestMethod.POST)
	public Page<TransactionNurseList> deleteNurseListForEveryTrx(@RequestParam("idTrx") Long idTrx, @RequestParam("id") Long id) {
		
		Page<TransactionNurseList> data = null;
		transactionListNurseDbRepository.deleteById(id);
		data = transactionListNurseDbRepository.findNurseListTrxWithPagination(idTrx, createPageRequest(0, 10, "ASC", "id"));
		logger.info("Fetching Nurse List for id trx "+ idTrx);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/transaction/getDoctorList/{id}", method = RequestMethod.GET)
	public Page<TransactionDoctorList> getAllDoctorListWithPagination(@PathVariable(value = "id") Long id) {
		
		Page<TransactionDoctorList> data = null;
		data = transactionListDoctorDbRepository.findDoctorListTrxWithPagination(id, createPageRequest(0, 10, "ASC", "id"));
		logger.info("Fetching Doctor List for id trx "+ id);
		return data;
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/transaction/getServicesList/{id}", method = RequestMethod.GET)
	public Page<TransactionServiceList> getAllServicesListWithPagination(@PathVariable(value = "id") Long id) {
		
		Page<TransactionServiceList> data = null;
		data = transactionListServicesDbRepository.findServicesListTrxWithPagination(id, createPageRequest(0, 10, "ASC", "id"));
		logger.info("Fetching services List for id trx "+ id);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC')")
	@RequestMapping(value = "/transaction/deleteDoctorList", method = RequestMethod.POST)
	public Page<TransactionDoctorList> deleteDoctorListForEveryTrx(@RequestParam("idTrx") Long idTrx, @RequestParam("id") Long id) {
		
		Page<TransactionDoctorList> data = null;
		transactionListDoctorDbRepository.deleteById(id);
		data = transactionListDoctorDbRepository.findDoctorListTrxWithPagination(idTrx, createPageRequest(0, 10, "ASC", "id"));
		logger.info("Fetching Doctor List for id trx "+ idTrx);
		return data;
	}
	
}
