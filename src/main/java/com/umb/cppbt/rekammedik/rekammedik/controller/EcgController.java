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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umb.cppbt.rekammedik.rekammedik.domain.Ecg;
import com.umb.cppbt.rekammedik.rekammedik.domain.ResponMessage;
import com.umb.cppbt.rekammedik.rekammedik.repository.EcgDbRepository;
import com.umb.cppbt.rekammedik.rekammedik.repository.UserPatientDbRepository;

@RestController
@RequestMapping(value = "/api")
public class EcgController {

	public static final Logger logger = LoggerFactory.getLogger(EcgController.class);
	
	@Autowired
	private EcgDbRepository ecgDbRepository;
	
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
	@RequestMapping(value = "/ecgWithPaginationByEcgCodeAndDate", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllEcgWithPaginationByEcgCodeAndDate(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestParam("sortField") String sortField, @RequestParam("ecgCode") String ecgCode, @RequestParam("date") String date, @RequestHeader(value="Authorization") String token) {
		
		Page<Ecg> data = null;
		String[] array = date.split("-");
		int year =  Integer.parseInt(array[0]);
		int month =  Integer.parseInt(array[1]);
		int day =  Integer.parseInt(array[2]);
		
		Long iduser = userPatientDbRepository.getPatientId(ecgCode);
		
		if(iduser == null){
			logger.info("device code is not found");
			ResponMessage error = new ResponMessage();
			error.setStatus(HttpStatus.NOT_FOUND);
			error.setMessage("device code "+ ecgCode +" is not found");
			return new ResponseEntity<Object>(error, new HttpHeaders() ,HttpStatus.NOT_FOUND);
		}
		
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		
		int idUserFromToken = (Integer) claims.get("id");
		List<String> roleUserFromToken = (List<String>) claims.get("roles");
		
		if(roleUserFromToken.contains("ROLE_PATIENT") == true){
			if(iduser == idUserFromToken){
				data = ecgDbRepository.findEcgDataWithPaginationByEcgCodeAndDate(ecgCode,day,month,year,createPageRequest(page, size, sort, sortField));
				logger.info("Fetching Ecg value with pagination order by " + sortField + " " + sort +" by id Ecg Code "+ ecgCode);
				return new ResponseEntity<Object>(data, new HttpHeaders() ,HttpStatus.OK);
			}
			else{
				data= null;
				logger.info("Fetching Ecg value is unauthorized");
				ResponMessage error = new ResponMessage();
				error.setStatus(HttpStatus.UNAUTHORIZED);
				error.setMessage("Fetching Ecg value with device code "+ ecgCode +" is UNAUTHORIZED, Your role is PATIENT");
				return new ResponseEntity<Object>(error, new HttpHeaders() ,HttpStatus.UNAUTHORIZED);
			}
		}else{
			data = ecgDbRepository.findEcgDataWithPaginationByEcgCodeAndDate(ecgCode,day,month,year,createPageRequest(page, size, sort, sortField));
			logger.info("Fetching Ecg value with pagination order by " + sortField + " " + sort +" by id Ecg Code "+ ecgCode);
			return new ResponseEntity<Object>(data, new HttpHeaders() ,HttpStatus.OK);
		}
	
	}
	
}
