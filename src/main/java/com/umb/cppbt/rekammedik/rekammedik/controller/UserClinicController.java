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
import com.umb.cppbt.rekammedik.rekammedik.domain.UserClinic;
import com.umb.cppbt.rekammedik.rekammedik.repository.UserClinicDbRepository;;


@RestController
@RequestMapping(value = "/api")
public class UserClinicController {

	public static final Logger logger = LoggerFactory.getLogger(UserClinicController.class);
	
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
	
	@PreAuthorize("hasAnyRole('ADMIN','CLINIC')")
	@RequestMapping(value = "/userClinic/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getuserClinicById(@PathVariable Long id, @RequestHeader(value="Authorization") String token)
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		UserClinic appUser = null;
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
			
			appUser = userClinicDbRepository.getOne(id);	
			
			if (appUser != null){
				logger.info("fetching user clinic with id " + appUser.getId());
				return new ResponseEntity<Object>(appUser, new HttpHeaders() ,HttpStatus.OK);
			}
			else{
				logger.info("user clinic not found");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.NOT_FOUND);
				error.setMessage("users clinic NOT_FOUND");
				return new ResponseEntity<Object>(error ,new HttpHeaders() , HttpStatus.NOT_FOUND);
			}
		}
		else{
			logger.info("users clinic UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user clinic with id "+ id +" is UNAUTHORIZED");

			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','CLINIC')")
	@RequestMapping(value = "/userClinic/{id}", method = RequestMethod.PUT )
	public ResponseEntity<Object> updateuserClinic(@PathVariable(value = "id") Long id,@RequestBody UserClinic userClinic ,  @RequestHeader(value="Authorization") String token) 
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
					
			UserClinic findFirst = userClinicDbRepository.getOne(id);
			UserClinic cekEmail = userClinicDbRepository.findUserClinicByEmail(userClinic.getEmail());
			
			String mes = null ;
			
			if(findFirst == null) {
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.NOT_FOUND);
				message.setMessage("user clinic with id "+ id + " NOT_FOUND");
				return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.NOT_FOUND);
		    }
			
			if(userClinic.getEmail() != null && cekEmail == null) findFirst.setEmail(userClinic.getEmail());
			if(userClinic.getPassword() != null) findFirst.setPassword(userClinic.getPassword());
			if(userClinic.getAddress() != null) findFirst.setAddress(userClinic.getAddress());
			if(userClinic.getPlaceBirth() != null) findFirst.setPlaceBirth(userClinic.getPlaceBirth());
			if(userClinic.getDateBirth() != null) findFirst.setDateBirth(userClinic.getDateBirth());
			if(userClinic.getFullName() != null) findFirst.setFullName(userClinic.getFullName());
			if(userClinic.getPhoneNumber() != null) findFirst.setPhoneNumber(userClinic.getPhoneNumber());
			if(userClinic.getGender() != null) findFirst.setGender(userClinic.getGender());
			if(userClinic.getReligion() != null)findFirst.setReligion(userClinic.getReligion());
			if(userClinic.getPhotoPath() != null)findFirst.setPhotoPath(userClinic.getPhotoPath());
			if(userClinic.getFirstRegistrationDate() != null)findFirst.setFirstRegistrationDate(userClinic.getFirstRegistrationDate());
			if(userClinic.getLatitude() != null)findFirst.setLatitude(userClinic.getLatitude());
			if(userClinic.getLongitude() != null)findFirst.setLongitude(userClinic.getLongitude());
			if(userClinic.getLongitude() != null)findFirst.setLongitude(userClinic.getLongitude());
			if(userClinic.getUserCode() != null)findFirst.setUserCode(userClinic.getUserCode());
			if(userClinic.getStatus() != null)findFirst.setStatus(userClinic.getStatus());
			if(userClinic.getClinic() != null) findFirst.setClinic(userClinic.getClinic());
			if(cekEmail != null){
				mes = "Succesfully Update user clinic with id "+ findFirst.getId() + ", but username '"+ userClinic.getEmail()  +"' that you input is already exist";
			}
			else{
				mes = "Succesfully Update user clinic with id "+ findFirst.getId();
			}
			userClinicDbRepository.save(findFirst);
			
			ResponMessage message = new ResponMessage();
			message.setStatus(HttpStatus.OK);
			message.setMessage(mes);
			
			return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("user clinic UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user clinic with id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','CLINIC')")
	@RequestMapping(value = "/userClinic/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteUser(@PathVariable Long id, @RequestHeader(value="Authorization") String token) {
		
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		UserClinic userClinic = null;
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
			userClinic = userClinicDbRepository.getOne(id);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loggedUsername = auth.getName();
			if (userClinic == null){
				logger.info("user clinic NOT FOUND");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.NOT_FOUND);
				error.setMessage("user clinic NOT FOUND");
				return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.NOT_FOUND);
			}
			else if (userClinic.getEmail().equalsIgnoreCase(loggedUsername)){
				//throw new RuntimeException("You cannot delete your account");
				logger.info("You cannot delete your account");
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.OK);
				message.setMessage("You cannot delete your account");
				return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
				
			}
			else{
				userClinicDbRepository.deleteById(id);
				logger.info("user clinic sucessfully deleted");
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.OK);
				message.setMessage("user clinic with id "+id+" sucessfully deleted");
				return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
			}
		}
		else{
			logger.info("user clinic UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user clinic with id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/userClinicsWithPagination", method = RequestMethod.GET)
	public Page<UserClinic> getAllUserClinicsWithPagination(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField) {
		Page<UserClinic> data = userClinicDbRepository.findUserClinicWithPagination(createPageRequest(page, size, sort, sortField));
		logger.info("Fetching All User Clinic Details with pagination order by " + sortField + " " + sort);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/userClinicsWithPaginationByIdClinic", method = RequestMethod.GET)
	public Page<UserClinic> getAllUserClinicsWithPaginationByIdClinic(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("clinicId") Integer clinicId) {
		Long id = new Long(clinicId);
		Page<UserClinic> data = userClinicDbRepository.findUserClinicWithPaginationByIdClinic(id,createPageRequest(page, size, sort, sortField));
		logger.info("Fetching All User Clinic Details with pagination order by " + sortField + " " + sort +" by id clinic "+ id);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/userClinicsWithPaginationByField", method = RequestMethod.GET)
	public Page<UserClinic> getAllUserClinicsWithPaginationByField(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value) {
		
		Page<UserClinic> data = null;
		
		if(searchField.equals("fullName")){
			data = userClinicDbRepository.findUserClinicByFullName(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("id")){
			Long id = new Long(value);
			data = userClinicDbRepository.findUserClinicById(id, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("nurseCode")){
			data = userClinicDbRepository.findUserClinicByUserClinicCode(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("email")){
			data = userClinicDbRepository.findUserClinicByEmail(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("phoneNumber")){
			data = userClinicDbRepository.findUserClinicByPhoneNumber(value, createPageRequest(page, size, sort, sortField));
		}
		
		
		logger.info("Fetching User Clinic with "+ searchField +" order by " + sortField + " " + sort);
		
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/userClinicsWithPaginationByFieldByIdClinic", method = RequestMethod.GET)
	public Page<UserClinic> getAllUserClinicsWithPaginationByFieldByIdClinic(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value, @RequestParam("clinicId") Integer clinicId) {
		
		Page<UserClinic> data = null;
		Long idClinic = new Long(clinicId);
		
		if(searchField.equals("fullName")){
			data = userClinicDbRepository.findUserClinicByFullNameByIdClinic(value, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("id")){
			Long id = new Long(value);
			data = userClinicDbRepository.findUserClinicByIdByIdClinic(id, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("doctorCode")){
			data = userClinicDbRepository.findUserClinicByUserClinicCodeByIdClinic(value, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("email")){
			data = userClinicDbRepository.findUserClinicByEmailByIdClinic(value, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("phoneNumber")){
			data = userClinicDbRepository.findUserClinicByPhoneNumberByIdClinic(value, idClinic ,createPageRequest(page, size, sort, sortField));
		}
		
		logger.info("Fetching User Clinic with "+ searchField +" order by " + sortField + " " + sort);
		
		return data;
	}
	
	
}
