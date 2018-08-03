package com.umb.cppbt.rekammedik.rekammedik.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umb.cppbt.rekammedik.rekammedik.domain.ResponMessage;
import com.umb.cppbt.rekammedik.rekammedik.domain.UserPatient;
import com.umb.cppbt.rekammedik.rekammedik.repository.UserPatientDbRepository;


@RestController
@RequestMapping(value = "/api")
public class UserPatientController {

	public static final Logger logger = LoggerFactory.getLogger(UserPatientController.class);
	
	@Autowired
	private UserPatientDbRepository userPatientDbRepository;
	
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
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/userPatient/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getuserPatientById(@PathVariable Long id, @RequestHeader(value="Authorization") String token)
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		UserPatient appUser = null;
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
			appUser = userPatientDbRepository.getOne(id);	
			if (appUser != null){
				logger.info("fetching user patient with id " + appUser.getId());
				return new ResponseEntity<Object>(appUser, new HttpHeaders() ,HttpStatus.OK);
			}
			else{
				logger.info("user patient not found");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.NOT_FOUND);
				error.setMessage("users patient NOT_FOUND");
				return new ResponseEntity<Object>(error ,new HttpHeaders() , HttpStatus.NOT_FOUND);
			}
		}
		else{
			logger.info("users patient UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user patient with id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/userPatient/{id}", method = RequestMethod.PUT )
	public ResponseEntity<Object> updateuserPatient(@PathVariable(value = "id") Long id,@RequestBody UserPatient userPatient,  @RequestHeader(value="Authorization") String token) 
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true || roleUserFromToken.contains("ROLE_CLINIC") == true || roleUserFromToken.contains("ROLE_NURSE") == true){
					
			UserPatient findFirst = userPatientDbRepository.getOne(id);
			UserPatient cekEmail = userPatientDbRepository.findByEmail(userPatient.getEmail());
			
			String mes = null ;
			
			if(findFirst == null) {
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.NOT_FOUND);
				message.setMessage("user patient with id "+ id + " NOT_FOUND");
				return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.NOT_FOUND);
		    }
			
			if(userPatient.getEmail() != null && cekEmail == null) findFirst.setEmail(userPatient.getEmail());
			if(userPatient.getPassword() != null) findFirst.setPassword(userPatient.getPassword());
			if(userPatient.getAddress() != null) findFirst.setAddress(userPatient.getAddress());
			if(userPatient.getPlaceBirth() != null) findFirst.setPlaceBirth(userPatient.getPlaceBirth());
			if(userPatient.getDateBirth() != null) findFirst.setDateBirth(userPatient.getDateBirth());
			if(userPatient.getFullName() != null) findFirst.setFullName(userPatient.getFullName());
			if(userPatient.getPhoneNumber() != null) findFirst.setPhoneNumber(userPatient.getPhoneNumber());
			if(userPatient.getGender() != null) findFirst.setGender(userPatient.getGender());
			if(userPatient.getReligion() != null)findFirst.setReligion(userPatient.getReligion());
			if(userPatient.getPhotoPath() != null)findFirst.setPhotoPath(userPatient.getPhotoPath());
			if(userPatient.getFirstRegistrationDate() != null)findFirst.setFirstRegistrationDate(userPatient.getFirstRegistrationDate());
			if(userPatient.getLatitude() != null)findFirst.setLatitude(userPatient.getLatitude());
			if(userPatient.getLongitude() != null)findFirst.setLongitude(userPatient.getLongitude());
			if(userPatient.getLongitude() != null)findFirst.setLongitude(userPatient.getLongitude());
			if(userPatient.getPatientCode() != null)findFirst.setPatientCode(userPatient.getPatientCode());
			if(userPatient.getStatus() != null)findFirst.setStatus(userPatient.getStatus());
			if(userPatient.getOccupation() != null) findFirst.setOccupation(userPatient.getOccupation());
			if(userPatient.getClinic() != null) findFirst.setClinic(userPatient.getClinic());
			if(userPatient.getDeviceCode() != null) findFirst.setDeviceCode(userPatient.getDeviceCode());
			if(cekEmail != null){
				mes = "Succesfully Update user patient with id "+ findFirst.getId() + ", but username '"+ userPatient.getEmail()  +"' that you input is already exist";
			}
			else{
				mes = "Succesfully Update user patient with id "+ findFirst.getId();
			}
			userPatientDbRepository.save(findFirst);
			
			ResponMessage message = new ResponMessage();
			message.setStatus(HttpStatus.OK);
			message.setMessage(mes);
			
			return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("user patient UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user patient with id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR', 'PATIENT')")
	@RequestMapping(value = "/userPatient/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteUser(@PathVariable Long id, @RequestHeader(value="Authorization") String token) {
		
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		UserPatient userPatient = null;
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
			userPatient = userPatientDbRepository.getOne(id);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loggedUsername = auth.getName();
			if (userPatient == null){
				logger.info("user patient NOT FOUND");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.NOT_FOUND);
				error.setMessage("user patient NOT FOUND");
				return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.NOT_FOUND);
			}
			else if (userPatient.getEmail().equalsIgnoreCase(loggedUsername)){
				//throw new RuntimeException("You cannot delete your account");
				logger.info("You cannot delete your account");
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.OK);
				message.setMessage("You cannot delete your account");
				return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
			}
			else{
				userPatientDbRepository.deleteById(id);
				logger.info("user patient sucessfully deleted");
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.OK);
				message.setMessage("user patient with id "+id+" sucessfully deleted");
				return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
			}
		}
		else{
			logger.info("user patient UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user patient with id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/patiensWithPagination", method = RequestMethod.GET)
	public Page<UserPatient> getAllPatientsWithPagination(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField) {
		Page<UserPatient> data = userPatientDbRepository.findPatientWithPagination(createPageRequest(page, size, sort, sortField));
		logger.info("Fetching All Patient Details with pagination order by " + sortField + " " + sort);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/patiensWithPaginationByIdClinic", method = RequestMethod.GET)
	public Page<UserPatient> getAllPatientsWithPaginationByIdClinic(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("clinicId") Integer clinicId) {
		Long id = new Long(clinicId);
		Page<UserPatient> data = userPatientDbRepository.findPatientWithPaginationByIdClinic(id,createPageRequest(page, size, sort, sortField));
		logger.info("Fetching All Patient Details with pagination order by " + sortField + " " + sort +" by id clinic "+ id);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/patiensWithPaginationByField", method = RequestMethod.GET)
	public Page<UserPatient> getAllUsersWithPaginationByField(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value) {
		
		Page<UserPatient> data = null;
		
		if(searchField.equals("fullName")){
			data = userPatientDbRepository.findPatientByFullName(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("id")){
			Long id = new Long(value);
			data = userPatientDbRepository.findPatientById(id, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("patientCode")){
			data = userPatientDbRepository.findPatientByPatientCode(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("email")){
			data = userPatientDbRepository.findPatientByEmail(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("phoneNumber")){
			data = userPatientDbRepository.findPatientByPhoneNumber(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("deviceCode")){
			data = userPatientDbRepository.findPatientByDeviceCode(value, createPageRequest(page, size, sort, sortField));
		}
		
		logger.info("Fetching patients with "+ searchField +" order by " + sortField + " " + sort);
		
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/patiensWithPaginationByFieldByIdClinic", method = RequestMethod.GET)
	public Page<UserPatient> getAllUsersWithPaginationByFieldByIdClinic(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value, @RequestParam("clinicId") Integer clinicId) {
		
		Page<UserPatient> data = null;
		Long idClinic = new Long(clinicId);
		
		if(searchField.equals("fullName")){
			data = userPatientDbRepository.findPatientByFullNameByIdClinic(value, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("id")){
			Long id = new Long(value);
			data = userPatientDbRepository.findPatientByIdByIdClinic(id, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("patientCode")){
			data = userPatientDbRepository.findPatientByPatientCodeByIdClinic(value, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("email")){
			data = userPatientDbRepository.findPatientByEmailByIdClinic(value, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("phoneNumber")){
			data = userPatientDbRepository.findPatientByPhoneNumberByIdClinic(value, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("deviceCode")){
			data = userPatientDbRepository.findPatientByDeviceCodeByIdClinic(value, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		
		logger.info("Fetching patients with "+ searchField +" order by " + sortField + " " + sort);
		
		return data;
	}
	
}
