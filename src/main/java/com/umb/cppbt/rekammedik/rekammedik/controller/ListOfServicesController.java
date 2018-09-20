package com.umb.cppbt.rekammedik.rekammedik.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.List;

import javax.persistence.IdClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.data.domain.Page;

import com.umb.cppbt.rekammedik.rekammedik.domain.ListOfServices;
import com.umb.cppbt.rekammedik.rekammedik.domain.ResponMessage;
import com.umb.cppbt.rekammedik.rekammedik.domain.Services;
import com.umb.cppbt.rekammedik.rekammedik.domain.UserDoctor;
import com.umb.cppbt.rekammedik.rekammedik.repository.ListOfServiceDbRepository;
import com.umb.cppbt.rekammedik.rekammedik.repository.ServiceDbRepository;
import com.umb.cppbt.rekammedik.rekammedik.repository.UserClinicDbRepository;


@RestController
@RequestMapping(value = "/api")
public class ListOfServicesController {
	
	public static final Logger logger = LoggerFactory.getLogger(ListOfServicesController.class);
	
	@Autowired
	private ListOfServiceDbRepository listOfServiceDbRepository;
	
	@Autowired
	private UserClinicDbRepository userClinicDbRepository;
	
	@Autowired
	private ServiceDbRepository serviceDbRepository;
	
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
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PATIENT', 'ROLE_CLINIC')")
	@RequestMapping(value = "/listOfservices/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllServicesByIdClinic(@PathVariable(value = "id") Long id) 
	{
		List<Services> services = listOfServiceDbRepository.findByClinicIdWithCustomQuery(id);
	
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
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(value = "/listOfservicesWithPagination", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllServicesWithPagination(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestHeader(value="Authorization") String token) 
	{
		Page<ListOfServices> listServices = listOfServiceDbRepository.findByClinicIdWithCustomQueryWithPaginationForAdmin(createPageRequest(page, size, sort, sortField));
		if(listServices == null){
			String msg = "Sorry, Data that you search is empty !";
			ResponMessage message =  new ResponMessage();
			message.setMessage(msg);
			message.setStatus(HttpStatus.NOT_FOUND);
			logger.info(msg);
			return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.NOT_FOUND);
		}
		else{
			logger.info("Fetching All List Of Services only for admin");
			return new ResponseEntity<Object>(listServices, new HttpHeaders() ,HttpStatus.OK);
		}
		
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLINIC')")
	@RequestMapping(value = "/listOfservicesWithPaginationByIdClinic", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllServicesWithPaginationByIdClinic(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("clinicId") Long clinicId, @RequestHeader(value="Authorization") String token) 
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		int idUserClinicFromToken = (Integer) claims.get("id");
		Long idClinicTarget = clinicId;
		Long idClinicActual = userClinicDbRepository.findIdClinicByIdUserClinic(new Long(idUserClinicFromToken));
		
		if(idClinicActual.equals(idClinicTarget)){
			Page<Services> services = listOfServiceDbRepository.findByClinicIdWithCustomQueryWithPagination(clinicId, createPageRequest(page, size, sort, sortField));
			if(services == null){
				String msg = "Sorry, Data that you search is empty !";
				ResponMessage message =  new ResponMessage();
				message.setMessage(msg);
				message.setStatus(HttpStatus.NOT_FOUND);
				logger.info(msg);
				return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.NOT_FOUND);
			}
			else{
				logger.info("Fetching All List Of Services by id clinic " + clinicId);
				return new ResponseEntity<Object>(services, new HttpHeaders() ,HttpStatus.OK);
			}
		}else{
			logger.info("UNAUTHORIZED, You can not see another data outside you clinic !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("UNAUTHORIZED, You can not see another data with id clinic = " + idClinicTarget + ". You can only see data with id clinic = "+idClinicActual + " !");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLINIC')")
	@RequestMapping(value = "/listOfservicesWithPaginationBySearchFieldByIdClinic", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllServicesWithPaginationBySearchFieldByIdClinic(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value, @RequestParam("clinicId") Long clinicId, @RequestHeader(value="Authorization") String token) 
	{
		Page<Services> services = null;
		
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		int idUserClinicFromToken = (Integer) claims.get("id");
		Long idClinicTarget = new Long(clinicId);
		Long idClinicActual = userClinicDbRepository.findIdClinicByIdUserClinic(new Long(idUserClinicFromToken));
		
		if(idClinicActual.equals(idClinicTarget)){
			if(searchField.equals("nameOfservices")){
				services = listOfServiceDbRepository.findByClinicIdWithCustomQueryWithPaginationByName(clinicId, value, createPageRequest(page, size, sort, sortField));
				logger.info("Fetching All List Of Services by name " + value);
			}
			else if(searchField.equals("codeOfservices")){
				services = listOfServiceDbRepository.findByClinicIdWithCustomQueryWithPaginationByCode(clinicId, value, createPageRequest(page, size, sort, sortField));
				logger.info("Fetching All List Of Services by code " + value);
			}
			else if(searchField.equals("price")){
				services = listOfServiceDbRepository.findByClinicIdWithCustomQueryWithPaginationByPrice(clinicId, new Float(value), createPageRequest(page, size, sort, sortField));
				logger.info("Fetching All List Of Services by price " + value);
			}
			return new ResponseEntity<Object>(services, new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("UNAUTHORIZED, You can not see another data outside you clinic !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("UNAUTHORIZED, You can not see another data with id clinic = " + idClinicTarget + ". You can only see data with id clinic = "+idClinicActual + " !");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(value = "/listOfservicesWithPaginationBySearchField", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllServicesWithPaginationBySearchField(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value) 
	{
		Page<ListOfServices> listOfservices = null;
		if(searchField.equals("nameOfservices")){
			listOfservices = listOfServiceDbRepository.findByClinicIdWithCustomQueryWithPaginationByNameForAdmin(value, createPageRequest(page, size, sort, sortField));
			logger.info("Fetching All List Of Services by name " + value);
		}
		else if(searchField.equals("codeOfservices")){
			listOfservices = listOfServiceDbRepository.findByClinicIdWithCustomQueryWithPaginationByCodeForAdmin(value, createPageRequest(page, size, sort, sortField));
			logger.info("Fetching All List Of Services by code " + value);
		}
		else if(searchField.equals("price")){
			listOfservices = listOfServiceDbRepository.findByClinicIdWithCustomQueryWithPaginationByPriceForAdmin(new Float(value), createPageRequest(page, size, sort, sortField));
			logger.info("Fetching All List Of Services by price " + value);
		}
		return new ResponseEntity<Object>(listOfservices, new HttpHeaders() ,HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLINIC')")
	@RequestMapping(value = "/deleteListOfServicesByIdClinicByIdService", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteListOfServicesByIdClinicByIdService(@RequestParam("serviceId") Long serviceId, @RequestParam("clinicId") Long clinicId, @RequestHeader(value="Authorization") String token) 
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		int idUserClinicFromToken = (Integer) claims.get("id");
		Long idClinicTarget = new Long(clinicId);
		Long idClinicActual = userClinicDbRepository.findIdClinicByIdUserClinic(new Long(idUserClinicFromToken));
		
		if(idClinicActual.equals(idClinicTarget)){
			listOfServiceDbRepository.deleteListOfServicesByIdClinicByIdService(clinicId, serviceId);
			logger.info("OK, delete data succesfull !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.OK);
			error.setMessage("OK, delete data succesfull with id clinic = " + clinicId  +" and id service = " + serviceId +".");
			return new ResponseEntity<Object>(error, new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("UNAUTHORIZED, You can not delete another data outside you clinic !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("UNAUTHORIZED, You can not delete data with id clinic = " + idClinicTarget + ". You can only delete data with id clinic = "+idClinicActual + " !");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC')")
	@RequestMapping(value = "/listOfservices/{id}", method = RequestMethod.PUT )
	public ResponseEntity<Object> updateServices(@PathVariable(value = "id") Long id, @RequestParam("clinicId") Long clinicId, @RequestBody Services services,  @RequestHeader(value="Authorization") String token) 
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		int idUserClinicFromToken = (Integer) claims.get("id");
		Long idClinicTarget = new Long(clinicId);
		Long idClinicActual = userClinicDbRepository.findIdClinicByIdUserClinic(new Long(idUserClinicFromToken));
		
		if(idClinicActual.equals(idClinicTarget)){
			Services svcTmp = serviceDbRepository.getOne(id);
			if(services.getNameOfservices() != null){svcTmp.setNameOfservices(services.getNameOfservices());}
			if(services.getCodeOfservices() != null){svcTmp.setCodeOfservices(services.getCodeOfservices());}
			if(services.getPrice() != null){svcTmp.setPrice(services.getPrice());}
			serviceDbRepository.save(svcTmp);
			logger.info("OK, update data succesfull !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.OK);
			error.setMessage("OK, update data succesfull with id clinic = " + clinicId  +" and id service = " + id +".");
			return new ResponseEntity<Object>(error, new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("UNAUTHORIZED, You can not update another data outside you clinic !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("UNAUTHORIZED, You can not update data with id clinic = " + clinicId + ". You can only delete data with id clinic = "+idClinicActual + " !");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC')")
	@RequestMapping(value = "/listOfservices", method = RequestMethod.POST)
	public ResponseEntity<Object> addServices(@RequestParam("clinicId") Long clinicId, @RequestBody ListOfServices listOfServices,  @RequestHeader(value="Authorization") String token) 
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		int idUserClinicFromToken = (Integer) claims.get("id");
		Long idClinicTarget = new Long(clinicId);
		Long idClinicActual = userClinicDbRepository.findIdClinicByIdUserClinic(new Long(idUserClinicFromToken));
		
		if(idClinicActual.equals(idClinicTarget)){
			
			Services svc = serviceDbRepository.save(listOfServices.getServices());
			listOfServices.setServices(svc);
			listOfServiceDbRepository.save(listOfServices);
			logger.info("OK, add data succesfull !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.OK);
			error.setMessage("OK, add data succesfull");
			return new ResponseEntity<Object>(error, new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("UNAUTHORIZED, You can not add another data outside you clinic !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("UNAUTHORIZED, You can not add data with id clinic = " + clinicId + ". You can only add data with id clinic = "+idClinicActual + " !");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}

}
