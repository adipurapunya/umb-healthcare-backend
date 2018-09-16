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
import com.umb.cppbt.rekammedik.rekammedik.domain.UserNurse;
import com.umb.cppbt.rekammedik.rekammedik.repository.UserClinicDbRepository;
import com.umb.cppbt.rekammedik.rekammedik.repository.UserNurseDbRepository;


@RestController
@RequestMapping(value = "/api")
public class UserNurseController {

	public static final Logger logger = LoggerFactory.getLogger(UserNurseController.class);
	
	@Autowired
	private UserNurseDbRepository userNurseDbRepository;
	
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
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/userNurse/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getuserNurseById(@PathVariable Long id, @RequestHeader(value="Authorization") String token)
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		UserNurse appUser = null;
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
			
			appUser = userNurseDbRepository.getOne(id);	
			
			if (appUser != null){
				logger.info("fetching user nurse with id " + appUser.getId());
				return new ResponseEntity<Object>(appUser, new HttpHeaders() ,HttpStatus.OK);
			}
			else{
				logger.info("user nurse not found");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.NOT_FOUND);
				error.setMessage("users nurse NOT_FOUND");
				return new ResponseEntity<Object>(error ,new HttpHeaders() , HttpStatus.NOT_FOUND);
			}
		}
		else{
			logger.info("users nurse UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user nurse with id "+ id +" is UNAUTHORIZED");

			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/userNurse/{id}", method = RequestMethod.PUT )
	public ResponseEntity<Object> updateuserNurse(@PathVariable(value = "id") Long id,@RequestBody UserNurse userNurse,  @RequestHeader(value="Authorization") String token) 
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
					
			UserNurse findFirst = userNurseDbRepository.getOne(id);
			UserNurse cekEmail = userNurseDbRepository.findNurseByEmail(userNurse.getEmail());
			
			String mes = null ;
			
			if(findFirst == null) {
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.NOT_FOUND);
				message.setMessage("user nurse with id "+ id + " NOT_FOUND");
				return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.NOT_FOUND);
		    }
			
			if(userNurse.getEmail() != null && cekEmail == null) findFirst.setEmail(userNurse.getEmail());
			if(userNurse.getPassword() != null) findFirst.setPassword(userNurse.getPassword());
			if(userNurse.getAddress() != null) findFirst.setAddress(userNurse.getAddress());
			if(userNurse.getPlaceBirth() != null) findFirst.setPlaceBirth(userNurse.getPlaceBirth());
			if(userNurse.getDateBirth() != null) findFirst.setDateBirth(userNurse.getDateBirth());
			if(userNurse.getFullName() != null) findFirst.setFullName(userNurse.getFullName());
			if(userNurse.getPhoneNumber() != null) findFirst.setPhoneNumber(userNurse.getPhoneNumber());
			if(userNurse.getGender() != null) findFirst.setGender(userNurse.getGender());
			if(userNurse.getReligion() != null)findFirst.setReligion(userNurse.getReligion());
			if(userNurse.getPhotoPath() != null)findFirst.setPhotoPath(userNurse.getPhotoPath());
			if(userNurse.getFirstRegistrationDate() != null)findFirst.setFirstRegistrationDate(userNurse.getFirstRegistrationDate());
			if(userNurse.getLatitude() != null)findFirst.setLatitude(userNurse.getLatitude());
			if(userNurse.getLongitude() != null)findFirst.setLongitude(userNurse.getLongitude());
			if(userNurse.getLongitude() != null)findFirst.setLongitude(userNurse.getLongitude());
			if(userNurse.getNurseCode() != null)findFirst.setNurseCode(userNurse.getNurseCode());
			if(userNurse.getStatus() != null)findFirst.setStatus(userNurse.getStatus());
			if(userNurse.getSipp() != null) findFirst.setSipp(userNurse.getSipp());
			if(userNurse.getClinic() != null) findFirst.setClinic(userNurse.getClinic());
			if(cekEmail != null){
				mes = "Succesfully Update user nurse with id "+ findFirst.getId() + ", but username '"+ userNurse.getEmail()  +"' that you input is already exist";
			}
			else{
				mes = "Succesfully Update user nurse with id "+ findFirst.getId();
			}
			userNurseDbRepository.save(findFirst);
			
			ResponMessage message = new ResponMessage();
			message.setStatus(HttpStatus.OK);
			message.setMessage(mes);
			
			return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("user nurse UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user nurse with id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/userNurse/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteUser(@PathVariable Long id, @RequestHeader(value="Authorization") String token) {
		
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		UserNurse userNurse = null;
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
			userNurse = userNurseDbRepository.getOne(id);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loggedUsername = auth.getName();
			if (userNurse == null){
				logger.info("user nurse NOT FOUND");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.NOT_FOUND);
				error.setMessage("user nurse NOT FOUND");
				return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.NOT_FOUND);
			}
			else if (userNurse.getEmail().equalsIgnoreCase(loggedUsername)){
				//throw new RuntimeException("You cannot delete your account");
				logger.info("You cannot delete your account");
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.OK);
				message.setMessage("You cannot delete your account");
				return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
				
			}
			else{
				userNurseDbRepository.deleteById(id);
				logger.info("user nurse sucessfully deleted");
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.OK);
				message.setMessage("user nurse with id "+id+" sucessfully deleted");
				return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
			}
		}
		else{
			logger.info("user nurse UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user nurse with id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/nursesWithPagination", method = RequestMethod.GET)
	public Page<UserNurse> getAllNursesWithPagination(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField) {
		Page<UserNurse> data = userNurseDbRepository.findNurseWithPagination(createPageRequest(page, size, sort, sortField));
		logger.info("Fetching All Nurses Details with pagination order by " + sortField + " " + sort);
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/nursesWithPaginationByIdClinic", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllNursesWithPaginationByIdClinic(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("clinicId") Integer clinicId, @RequestHeader(value="Authorization") String token) {
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		int idUserClinicFromToken = (Integer) claims.get("id");
		Long idClinicTarget = new Long(clinicId);
		Long idClinicActual = userClinicDbRepository.findIdClinicByIdUserClinic(new Long(idUserClinicFromToken));
		
		if(idClinicActual.equals(idClinicTarget)){
			Page<UserNurse> data = userNurseDbRepository.findNurseWithPaginationByIdClinic(idClinicTarget,createPageRequest(page, size, sort, sortField));
			logger.info("Fetching All Nurses Details with pagination order by " + sortField + " " + sort +" by id clinic "+ idClinicTarget);
			return new ResponseEntity<Object>(data , new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("UNAUTHORIZED, You can not see another data outside you clinic !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("UNAUTHORIZED, You can not see another data with id clinic = " + idClinicTarget + ". You can only see data with id clinic = "+idClinicActual + " !");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/nursesWithPaginationByField", method = RequestMethod.GET)
	public Page<UserNurse> getAllNursesWithPaginationByField(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value) {
		
		Page<UserNurse> data = null;
		
		if(searchField.equals("fullName")){
			data = userNurseDbRepository.findNurseByFullName(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("id")){
			Long id = new Long(value);
			data = userNurseDbRepository.findNurseById(id, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("nurseCode")){
			data = userNurseDbRepository.findNurseByNurseCode(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("email")){
			data = userNurseDbRepository.findNurseByEmail(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("phoneNumber")){
			data = userNurseDbRepository.findNurseByPhoneNumber(value, createPageRequest(page, size, sort, sortField));
		}
		else if(searchField.equals("sipp")){
			data = userNurseDbRepository.findNurseBySipp(value, createPageRequest(page, size, sort, sortField));
		}
		
		logger.info("Fetching nurse with "+ searchField +" order by " + sortField + " " + sort);
		
		return data;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLINIC', 'NURSE', 'DOCTOR')")
	@RequestMapping(value = "/nursesWithPaginationByFieldByIdClinic", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllNursesWithPaginationByFieldByIdClinic(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("searchField") String searchField, @RequestParam("value") String value, @RequestParam("clinicId") Integer clinicId, @RequestHeader(value="Authorization") String token) {
		
		Page<UserNurse> data = null;
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		int idUserClinicFromToken = (Integer) claims.get("id");
		Long idClinicTarget = new Long(clinicId);
		Long idClinicActual = userClinicDbRepository.findIdClinicByIdUserClinic(new Long(idUserClinicFromToken));
		
		if(idClinicActual.equals(idClinicTarget)){
			if(searchField.equals("fullName")){
				data = userNurseDbRepository.findNurseByFullNameByIdClinic(value, idClinicTarget ,createPageRequest(page, size, sort, sortField));
			}
			else if(searchField.equals("id")){
				Long id = new Long(value);
				data = userNurseDbRepository.findNurseByIdByIdClinic(id, idClinicTarget ,createPageRequest(page, size, sort, sortField));
			}
			else if(searchField.equals("patientCode")){
				data = userNurseDbRepository.findNurseByNurseCodeByIdClinic(value, idClinicTarget ,createPageRequest(page, size, sort, sortField));
			}
			else if(searchField.equals("email")){
				data = userNurseDbRepository.findNurseByEmailByIdClinic(value, idClinicTarget ,createPageRequest(page, size, sort, sortField));
			}
			else if(searchField.equals("phoneNumber")){
				data = userNurseDbRepository.findNurseByPhoneNumberByIdClinic(value, idClinicTarget ,createPageRequest(page, size, sort, sortField));
			}
			else if(searchField.equals("sipp")){
				data = userNurseDbRepository.findNurseBySippByIdClinic(value, idClinicTarget ,createPageRequest(page, size, sort, sortField));
			}
			logger.info("Fetching nurse with "+ searchField +" order by " + sortField + " " + sort);
			return new ResponseEntity<Object>(data , new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("UNAUTHORIZED, You can not see another data outside you clinic !");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("UNAUTHORIZED, You can not see another data with id clinic = " + idClinicTarget + ". You can only see data with id clinic = "+idClinicActual + " !");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	
}
