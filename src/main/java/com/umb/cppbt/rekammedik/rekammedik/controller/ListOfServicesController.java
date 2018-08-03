package com.umb.cppbt.rekammedik.rekammedik.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.umb.cppbt.rekammedik.rekammedik.domain.ResponMessage;
import com.umb.cppbt.rekammedik.rekammedik.domain.Services;
import com.umb.cppbt.rekammedik.rekammedik.repository.ListOfServiceDbRepository;


@RestController
@RequestMapping(value = "/api")
public class ListOfServicesController {
	
	public static final Logger logger = LoggerFactory.getLogger(ListOfServicesController.class);
	
	@Autowired
	private ListOfServiceDbRepository listOfServiceDbRepository;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PATIENT', 'ROLE_CLINIC')")
	@RequestMapping(value = "/listOfservices/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllServicesByIdClinic(@PathVariable(value = "id") Long id) 
	{
		//long lStartTime = System.nanoTime();
		//List<ListOfServices> services = listOfServiceDbRepository.findByClinicId(Long.parseLong("1"));
		List<Services> services = listOfServiceDbRepository.findByClinicIdWithCustomQuery(id);
		//long lEndTime = System.nanoTime();
		
		if(services.isEmpty()){
			String msg = "Sorry, Data that you search is empty !";
			ResponMessage message =  new ResponMessage();
			message.setMessage(msg);
			message.setStatus(HttpStatus.NOT_FOUND);
			logger.info(msg);
			return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.NOT_FOUND);
		}
		else{
			logger.info("Fetching All List Of Services by id clinic "+id);
			return new ResponseEntity<Object>(services, new HttpHeaders() ,HttpStatus.OK);
		}
		//time elapsed
        ///long output = lEndTime - lStartTime;
		//System.out.println("Elapsed time in milliseconds: " + output / 1000000);
		//return svc;
	}

}
