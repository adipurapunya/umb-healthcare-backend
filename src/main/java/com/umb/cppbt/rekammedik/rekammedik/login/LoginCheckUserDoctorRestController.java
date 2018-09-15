package com.umb.cppbt.rekammedik.rekammedik.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umb.cppbt.rekammedik.rekammedik.domain.ResponMessage;
import com.umb.cppbt.rekammedik.rekammedik.domain.Status;
import com.umb.cppbt.rekammedik.rekammedik.domain.UserDoctor;
import com.umb.cppbt.rekammedik.rekammedik.repository.UserDoctorDbRepository;


@RestController
public class LoginCheckUserDoctorRestController {
	
	@Autowired
	private UserDoctorDbRepository userDoctorDbRepository;
	
	private static final String IV =   "dc0da04af8fee58593442bf834b30739";
    private static final String SALT = "dc0da04af8fee58593442bf834b30739";
    private static final int KEY_SIZE = 128;
    private static final int ITERATION_COUNT = 1000;
    private static final String PASSPHRASE = "pptik2018pptik18";
    
    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;
    
	@RequestMapping(value = "/register/userDoctor", method = RequestMethod.POST /*, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE */)
	public ResponseEntity<Object> createUser(@RequestBody UserDoctor user) {
		if(userDoctorDbRepository.findDoctorByEmail(user.getEmail()) != null){
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("email is already exist");
			return new ResponseEntity<Object>(error ,new HttpHeaders() , HttpStatus.UNAUTHORIZED);
		}
		
		String passEncrypt = user.getPassword();
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_DOCTOR");
		user.setRoles(roles);
		user.setPassword(Encrypt(passEncrypt));
		Status sts = new Status();
		long id = 1;
		sts.setId(id);
		user.setStatus(sts);
		UserDoctor dataInsert = userDoctorDbRepository.save(user);
		user.setDoctorCode("DOC00"+dataInsert.getId());
		userDoctorDbRepository.save(user);
		
		return new ResponseEntity<Object>(dataInsert, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/authenticate/userDoctor", method = RequestMethod.POST)
	public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password, HttpServletResponse response) throws GeneralSecurityException {
		String token = null;
		String userPasswordDB = "";
		UserDoctor user = null;
		Map<String, Object> tokenMap = new HashMap<String, Object>();
		
		if( email != "" || password != "" ){
			try {
				user = userDoctorDbRepository.findDoctorByEmail(email);
			} 
			catch (Exception e) {
				System.out.println("Error Message" + e.getMessage());
			}
			
			if(user != null){
				userPasswordDB = user.getPassword();
			}
			else{
				//System.out.println("Empty");
			}
		}
		
		if (user != null && Decrypt(userPasswordDB).equals(password)) {
			
			Date exp = new Date(System.currentTimeMillis() + ( 10000 * EXPIRES_IN ));
			token = Jwts.builder()
					.setSubject(email)
					.setExpiration(exp)
					.claim("roles", user.getRoles())
					.claim("id", user.getId())
					.setIssuedAt(new Date())
					.signWith(SignatureAlgorithm.HS256, "secretkey")
					.compact();
			tokenMap.put("token", token);
			tokenMap.put("user", user);
			
			return new ResponseEntity<Object>(tokenMap, HttpStatus.OK);
		}
		else if(user == null){
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("INVALID EMAIL OR PASSWORD");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
		else{
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.UNAUTHORIZED);
			error.setMessage("UNAUTHORIZED REQUEST / INVALID EMAIL OR PASSWORD");
			return new ResponseEntity<Object>(error , new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
		}
	}
	
	public String  Decrypt(String CIPHER_TEXT) throws GeneralSecurityException {
        AesUtilHelper util = new AesUtilHelper(KEY_SIZE, ITERATION_COUNT);
        String decrypt = util.decrypt(SALT, IV, PASSPHRASE, CIPHER_TEXT);
        return decrypt;
    }
	
	public String Encrypt(String PLAIN_TEXT) {
        AesUtilHelper util = new AesUtilHelper(KEY_SIZE, ITERATION_COUNT);
        String encrypt = util.encrypt(SALT, IV, PASSPHRASE, PLAIN_TEXT);     
        return encrypt;
    }
	
}
