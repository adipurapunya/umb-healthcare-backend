package com.umb.cppbt.rekammedik.rekammedik.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.umb.cppbt.rekammedik.rekammedik.domain.Clinic;
import com.umb.cppbt.rekammedik.rekammedik.repository.ClinicDbRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping(value = "/api")
public class ClinicController {
	
	public static final Logger logger = LoggerFactory.getLogger(EcgController.class);
	
	@Autowired
	private ClinicDbRepository clinicDbRepository;
	

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
	public ResponseEntity<Object> getAllClinics() 
	{
		System.out.println("Email : "+findLoggedInEmail());
		System.out.println("Username : "+findLoggedInUsername());
		logger.info("Fetching All Clinics");
		return new ResponseEntity<Object>(clinicDbRepository.findAll(), HttpStatus.OK);
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
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLINIC')")
	@RequestMapping(value = "/clinic/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteClinic(@PathVariable Long id) {
		Clinic clinic = clinicDbRepository.getOne(id);
		if (clinic != null) {
			clinicDbRepository.deleteById(id);
			return new ResponseEntity<String>("Succesfully delete clinics with id "+id, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<String>("Failed delete clinics", HttpStatus.FAILED_DEPENDENCY);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLINIC')")
	@RequestMapping(value = "/clinic/{id}", method = RequestMethod.PUT )
	public ResponseEntity<String> updateClinic(@PathVariable(value = "id") Long id,@RequestBody Clinic clinic) 
	{	
		Clinic data = clinicDbRepository.getOne(id);	
		if(data == null) {
			return new ResponseEntity<String>("Not Found", HttpStatus.NOT_FOUND);
	    }
		else{
			data.setNameOfClinic(clinic.getNameOfClinic());
			data.setAddress(clinic.getAddress());
			data.setStatus(clinic.getStatus());
			clinicDbRepository.save(clinic);
			
			return new ResponseEntity<String>("Succesfully Update clinic with id "+id, HttpStatus.OK);
		}	
	}

}
