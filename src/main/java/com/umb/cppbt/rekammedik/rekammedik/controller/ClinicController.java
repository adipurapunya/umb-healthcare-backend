package com.umb.cppbt.rekammedik.rekammedik.controller;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umb.cppbt.rekammedik.rekammedik.domain.Clinic;
import com.umb.cppbt.rekammedik.rekammedik.domain.ResponMessage;
import com.umb.cppbt.rekammedik.rekammedik.domain.TransactionNurseList;
import com.umb.cppbt.rekammedik.rekammedik.repository.ClinicDbRepository;


@RestController
@RequestMapping(value = "/api")
public class ClinicController {
	
	public static final Logger logger = LoggerFactory.getLogger(EcgController.class);
	
	@Autowired
	private ClinicDbRepository clinicDbRepository;
	
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
	

    public String findLoggedInEmail() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        return name;
    }
    
    public String findLoggedInUsername() {
    	User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername(); //get logged in username
        return name;
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PATIENT', 'ROLE_CLINIC')")
	@RequestMapping(value = "/clinics", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllClinics() {
		logger.info("Fetching All Clinics");
		return new ResponseEntity<Object>(clinicDbRepository.findAllStatusActive(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/clinicsStatusActive", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllClinicsStatusActive() {
		logger.info("Fetching All Clinics");
		return new ResponseEntity<Object>(clinicDbRepository.findAllStatusActive(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/clinicsWithPagination", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllClinicsWithPagination(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField) {
		Page<Clinic> data = null;
		data = clinicDbRepository.findAll(createPageRequest(page, size, sort, sortField));
		logger.info("Fetching All Clinics with pagination page = "+page+" size = " + size + " sort = " + sort +" sortField "+ sortField);
		return new ResponseEntity<Object>(data, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/clinicsWithPaginationBySearchField", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllClinicsWithPaginationBySearchField(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value) {
		Page<Clinic> data = null;
		
		if(searchField.equals("nameOfClinic")){
			data = clinicDbRepository.findClinicByName(value,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("codeOfClinic")){
			data = clinicDbRepository.findClinicByClinicCode(value,createPageRequest(page, size, sort, sortField));
		}
		logger.info("Fetching All Clinics with pagination page = "+page+" size = " + size + " sort = " + sort +" sortField = "+ sortField + " searchField = " + searchField + " " + value);
		return new ResponseEntity<Object>(data , new HttpHeaders() ,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLINIC')")
	@RequestMapping(value = "/clinic/{id}", method = RequestMethod.GET)
	public ResponseEntity<Clinic> getClinicById(@PathVariable Long id){
		Clinic clinic = clinicDbRepository.getOne(id);
		if (clinic == null){
			logger.info("Clinic is null");
			return new ResponseEntity<Clinic>(HttpStatus.NOT_FOUND);
		}
		else{
			logger.info("fetching Clinic by id "+id);
			return new ResponseEntity<Clinic>(clinic, HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/clinic/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteClinic(@PathVariable Long id) {
		Clinic clinic = clinicDbRepository.getOne(id);
		if (clinic != null) {
			clinicDbRepository.deleteById(id);
			ResponMessage msg = new ResponMessage();
			msg.setStatus(HttpStatus.OK);
			msg.setMessage("Succesfully delete clinics with id "+id);
			return new ResponseEntity<Object>(msg, HttpStatus.OK);
		}
		else{
			ResponMessage msg = new ResponMessage();
			msg.setStatus(HttpStatus.OK);
			msg.setMessage("Failed delete clinics with id "+id);
			return new ResponseEntity<Object>("Failed delete clinics", HttpStatus.FAILED_DEPENDENCY);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC')")
	@RequestMapping(value = "/clinic", method = RequestMethod.POST)
	public ResponseEntity<Object> addClinic(@RequestBody Clinic clinic) {
		
		Clinic clc = clinicDbRepository.save(clinic);
		
		ResponMessage msg = new ResponMessage();
		msg.setStatus(HttpStatus.OK);
		msg.setMessage("Succesfully add clinic with id "+clc.getId());
		
		return new ResponseEntity<Object>(msg, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLINIC')")
	@RequestMapping(value = "/clinic/{id}", method = RequestMethod.PUT )
	public ResponseEntity<Object> updateClinic(@PathVariable(value = "id") Long id,@RequestBody Clinic clinic) 
	{	
		Clinic data = clinicDbRepository.getOne(id);	
		if(data == null) {
			ResponMessage msg = new ResponMessage();
			msg.setStatus(HttpStatus.NOT_FOUND);
			msg.setMessage("Not Found clinic with id "+id);
			return new ResponseEntity<Object>(msg, HttpStatus.NOT_FOUND);
	    }
		else{
			data.setNameOfClinic(clinic.getNameOfClinic());
			data.setCodeOfClinic(clinic.getCodeOfClinic());
			data.setAddress(clinic.getAddress());
			data.setStatus(clinic.getStatus());
			data.setStatusActive(clinic.getStatusActive());
			data.setChallengeCode(clinic.getChallengeCode());
			clinicDbRepository.save(data);
			
			ResponMessage msg = new ResponMessage();
			msg.setStatus(HttpStatus.OK);
			msg.setMessage("Succesfully Update clinic with id "+id);
			
			return new ResponseEntity<Object>(msg, HttpStatus.OK);
		}	
	}

}
