package com.umb.cppbt.rekammedik.rekammedik.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;
import com.umb.cppbt.rekammedik.rekammedik.domain.ResponMessage;
import com.umb.cppbt.rekammedik.rekammedik.domain.UserDoctor;
import com.umb.cppbt.rekammedik.rekammedik.repository.UserDoctorDbRepository;;


@RestController
@RequestMapping(value = "/api")
public class UserDoctorController {

	public static final Logger logger = LoggerFactory.getLogger(UserDoctorController.class);
	
	@Autowired
	private UserDoctorDbRepository userDoctorDbRepository;
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/userDoctor/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getuserDoctorById(@PathVariable Long id, @RequestHeader(value="Authorization") String token)
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		UserDoctor appUser = null;
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
			
			appUser = userDoctorDbRepository.getOne(id);	
			
			if (appUser != null){
				logger.info("fetching user doctor with id " + appUser.getId());
				return new ResponseEntity<Object>(appUser, new HttpHeaders() ,HttpStatus.OK);
			}
			else{
				logger.info("user doctor not found");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.NOT_FOUND);
				error.setMessage("users doctor NOT_FOUND");
				return new ResponseEntity<Object>(error ,new HttpHeaders() , HttpStatus.NOT_FOUND);
			}
		}
		else{
			logger.info("users doctor UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user doctor with id "+ id +" is UNAUTHORIZED");

			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/userDoctor/{id}", method = RequestMethod.PUT )
	public ResponseEntity<Object> updateuserDoctor(@PathVariable(value = "id") Long id,@RequestBody UserDoctor userDoctor,  @RequestHeader(value="Authorization") String token) 
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
					
			UserDoctor findFirst = userDoctorDbRepository.getOne(id);
			UserDoctor cekEmail = userDoctorDbRepository.findByEmail(userDoctor.getEmail());
			
			String mes = null ;
			
			if(findFirst == null) {
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.NOT_FOUND);
				message.setMessage("user doctor with id "+ id + " NOT_FOUND");
				return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.NOT_FOUND);
		    }
			
			if(userDoctor.getEmail() != null && cekEmail == null) findFirst.setEmail(userDoctor.getEmail());
			if(userDoctor.getPassword() != null) findFirst.setPassword(userDoctor.getPassword());
			if(userDoctor.getAddress() != null) findFirst.setAddress(userDoctor.getAddress());
			if(userDoctor.getPlaceBirth() != null) findFirst.setPlaceBirth(userDoctor.getPlaceBirth());
			if(userDoctor.getDateBirth() != null) findFirst.setDateBirth(userDoctor.getDateBirth());
			if(userDoctor.getFullName() != null) findFirst.setFullName(userDoctor.getFullName());
			if(userDoctor.getPhoneNumber() != null) findFirst.setPhoneNumber(userDoctor.getPhoneNumber());
			if(userDoctor.getGender() != null) findFirst.setGender(userDoctor.getGender());
			if(userDoctor.getReligion() != null)findFirst.setReligion(userDoctor.getReligion());
			if(userDoctor.getPhotoPath() != null)findFirst.setPhotoPath(userDoctor.getPhotoPath());
			if(userDoctor.getFirstRegistrationDate() != null)findFirst.setFirstRegistrationDate(userDoctor.getFirstRegistrationDate());
			if(userDoctor.getLatitude() != null)findFirst.setLatitude(userDoctor.getLatitude());
			if(userDoctor.getLongitude() != null)findFirst.setLongitude(userDoctor.getLongitude());
			if(userDoctor.getLongitude() != null)findFirst.setLongitude(userDoctor.getLongitude());
			if(userDoctor.getDoctorCode()!= null)findFirst.setDoctorCode(userDoctor.getDoctorCode());
			if(userDoctor.getStatus() != null)findFirst.setStatus(userDoctor.getStatus());
			if(userDoctor.getRegisterNumber() != null) findFirst.setRegisterNumber(userDoctor.getRegisterNumber());
			if(userDoctor.getSpecialist() != null) findFirst.setSpecialist(userDoctor.getSpecialist());
			if(userDoctor.getClinic() != null) findFirst.setClinic(userDoctor.getClinic());
			if(cekEmail != null){
				mes = "Succesfully Update user doctor with id "+ findFirst.getId() + ", but username '"+ userDoctor.getEmail()  +"' that you input is already exist";
			}
			else{
				mes = "Succesfully Update user doctor with id "+ findFirst.getId();
			}
			userDoctorDbRepository.save(findFirst);
			
			ResponMessage message = new ResponMessage();
			message.setStatus(HttpStatus.OK);
			message.setMessage(mes);
			
			return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("user doctor UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user doctor with id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN', 'ROLE_CLINIC')")
	@RequestMapping(value = "/userDoctor/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteUser(@PathVariable Long id, @RequestHeader(value="Authorization") String token) {
		
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		UserDoctor userDoctor = null;
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
			userDoctor = userDoctorDbRepository.getOne(id);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loggedUsername = auth.getName();
			if (userDoctor == null){
				logger.info("user doctor NOT FOUND");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.NOT_FOUND);
				error.setMessage("user doctor NOT FOUND");
				return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.NOT_FOUND);
			}
			else if (userDoctor.getEmail().equalsIgnoreCase(loggedUsername)){
				//throw new RuntimeException("You cannot delete your account");
				logger.info("You cannot delete your account");
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.OK);
				message.setMessage("You cannot delete your account");
				return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
				
			}
			else{
				userDoctorDbRepository.deleteById(id);
				logger.info("user doctor sucessfully deleted");
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.OK);
				message.setMessage("user doctor with id "+id+" sucessfully deleted");
				return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
			}
		}
		else{
			logger.info("user nurse UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user doctor with id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	
}