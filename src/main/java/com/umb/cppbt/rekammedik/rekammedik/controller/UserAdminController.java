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
import com.umb.cppbt.rekammedik.rekammedik.domain.UserAdmin;
import com.umb.cppbt.rekammedik.rekammedik.repository.UserAdminDbRepository;


@RestController
@RequestMapping(value = "/api")
public class UserAdminController {

	public static final Logger logger = LoggerFactory.getLogger(UserAdminController.class);
	
	@Autowired
	private UserAdminDbRepository userAdminDbRepository;
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/userAdmin/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getUserAdminById(@PathVariable Long id, @RequestHeader(value="Authorization") String token)
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		UserAdmin appUser = null;
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
			
			appUser = userAdminDbRepository.getOne(id);	
			
			if (appUser != null){
				logger.info("fetching user with id " + appUser.getId());
				return new ResponseEntity<Object>(appUser, new HttpHeaders() ,HttpStatus.OK);
			}
			else{
				logger.info("user not found");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.NOT_FOUND);
				error.setMessage("users NOT_FOUND");
				return new ResponseEntity<Object>(error ,new HttpHeaders() , HttpStatus.NOT_FOUND);
			}
		}
		else{
			logger.info("users UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user with id "+ id +" is UNAUTHORIZED");

			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/userAdmin/{id}", method = RequestMethod.PUT )
	public ResponseEntity<Object> updateUserAdmin(@PathVariable(value = "id") Long id,@RequestBody UserAdmin userAdmin ,  @RequestHeader(value="Authorization") String token) 
	{
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
					
			UserAdmin findFirst = userAdminDbRepository.getOne(id);
			UserAdmin cekEmail = userAdminDbRepository.findUserAdminByEmail(userAdmin.getEmail());
			
			String mes = null ;
			
			if(findFirst == null) {
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.NOT_FOUND);
				message.setMessage("user with id "+ id + " NOT_FOUND");
				return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.NOT_FOUND);
		    }
			
			if(userAdmin.getEmail() != null && cekEmail == null) findFirst.setEmail(userAdmin.getEmail());
			if(userAdmin.getPassword() != null) findFirst.setPassword(userAdmin.getPassword());
			if(userAdmin.getAddress() != null) findFirst.setAddress(userAdmin.getAddress());
			if(userAdmin.getPlaceBirth() != null) findFirst.setPlaceBirth(userAdmin.getPlaceBirth());
			if(userAdmin.getDateBirth() != null) findFirst.setDateBirth(userAdmin.getDateBirth());
			if(userAdmin.getFullName() != null) findFirst.setFullName(userAdmin.getFullName());
			if(userAdmin.getPhoneNumber() != null) findFirst.setPhoneNumber(userAdmin.getPhoneNumber());
			if(userAdmin.getGender() != null) findFirst.setGender(userAdmin.getGender());
			if(userAdmin.getReligion() != null)findFirst.setReligion(userAdmin.getReligion());
			if(userAdmin.getPhotoPath() != null)findFirst.setPhotoPath(userAdmin.getPhotoPath());
			if(userAdmin.getFirstRegistrationDate() != null)findFirst.setFirstRegistrationDate(userAdmin.getFirstRegistrationDate());
			if(userAdmin.getLatitude() != null)findFirst.setLatitude(userAdmin.getLatitude());
			if(userAdmin.getLongitude() != null)findFirst.setLongitude(userAdmin.getLongitude());
			if(userAdmin.getLongitude() != null)findFirst.setLongitude(userAdmin.getLongitude());
			if(userAdmin.getAdminCode() != null)findFirst.setAdminCode(userAdmin.getAdminCode());
			if(userAdmin.getStatus() != null)findFirst.setStatus(userAdmin.getStatus());
			if(cekEmail != null){
				mes = "Succesfully Update user admin with id "+ findFirst.getId() + ", but username '"+ userAdmin.getEmail()  +"' that you input is already exist";
			}
			else{
				mes = "Succesfully Update user admin with id "+ findFirst.getId();
			}
			userAdminDbRepository.save(findFirst);
			
			ResponMessage message = new ResponMessage();
			message.setStatus(HttpStatus.OK);
			message.setMessage(mes);
			
			return new ResponseEntity<Object>(message, new HttpHeaders() ,HttpStatus.OK);
		}else{
			logger.info("user UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user with admin id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN', 'ROLE_CLINIC')")
	@RequestMapping(value = "/userAdmin/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteUser(@PathVariable Long id, @RequestHeader(value="Authorization") String token) {
		
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		UserAdmin userAdmin = null;
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(id == idUserFromToken || roleUserFromToken.contains("ROLE_ADMIN") == true){
			userAdmin = userAdminDbRepository.getOne(id);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loggedUsername = auth.getName();
			if (userAdmin == null){
				logger.info("user NOT FOUND");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.NOT_FOUND);
				error.setMessage("user NOT FOUND");
				return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.NOT_FOUND);
			}
			else if (userAdmin.getEmail().equalsIgnoreCase(loggedUsername)){
				//throw new RuntimeException("You cannot delete your account");
				logger.info("You cannot delete your account");
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.OK);
				message.setMessage("You cannot delete your account");
				return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
				
			}
			else{
				userAdminDbRepository.deleteById(id);
				logger.info("user sucessfully deleted");
				ResponMessage message = new ResponMessage();
				message.setStatus(HttpStatus.OK);
				message.setMessage("user with id "+id+" sucessfully deleted");
				return new ResponseEntity<Object>(message , new HttpHeaders() ,HttpStatus.OK);
			}
		}
		else{
			logger.info("user UNAUTHORIZED");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("user with id "+ id +" is UNAUTHORIZED");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	
}
