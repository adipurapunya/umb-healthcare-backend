package com.umb.cppbt.rekammedik.rekammedik.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umb.cppbt.rekammedik.rekammedik.domain.ResponMessage;

/**
 * A generic filter for security. I will check token present in the header.
 */

public class JWTFilter extends GenericFilterBean {
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String AUTHORITIES_KEY = "roles";

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String authHeader = request.getHeader(AUTHORIZATION_HEADER);
		
		if (authHeader == null || !authHeader.startsWith("")) {
			ResponMessage msg = new ResponMessage();
			msg.setMessage("Invalid authorization header or method !");
			msg.setStatus(org.springframework.http.HttpStatus.UNAUTHORIZED);
			response.setStatus(HttpStatus.SC_UNAUTHORIZED);
			response.setContentType("application/json");
            response.getWriter().write(convertObjectToJson(msg));
		}
		else {
			String token = authHeader; 
			if (isTokenExpired(token)){
				//System.out.println("Expired");
				ResponMessage msg = new ResponMessage();
				msg.setMessage("Your session token is expired !");
				msg.setStatus(org.springframework.http.HttpStatus.FORBIDDEN);
				response.setStatus(HttpStatus.SC_FORBIDDEN);
				response.setContentType("application/json");
	            response.getWriter().write(convertObjectToJson(msg));
			}
			else{
				try {
					Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
					//System.out.println("Claims : "+claims.toString());
					claims.get("");
					request.setAttribute("claims", claims);
					SecurityContextHolder.getContext().setAuthentication(getAuthentication(claims));
					filterChain.doFilter(req, res);
				} 
				catch (SignatureException e) {
					ResponMessage msg = new ResponMessage();
					msg.setMessage("Invalid token, make sure you put the rigth token !");
					msg.setStatus(org.springframework.http.HttpStatus.UNAUTHORIZED);
					response.setStatus(HttpStatus.SC_UNAUTHORIZED);
					response.setContentType("application/json");
		            response.getWriter().write(convertObjectToJson(msg));
				}
			}
		}
	}
	
	public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
	
	public Boolean isTokenExpired(String token) 
	{
	   final Date expiration = getExpirationDateFromToken(token);
	   //System.out.println("Expire : "+expiration);
	   boolean data = false;
	   
	   if(expiration==null){
		   data = true;
	   }
	   else{
		   data = false;
	   }
	   
	   return data;
	}
	
	public Date getExpirationDateFromToken(String token) 
    {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } 
        catch (Exception e){
            expiration = null;
        }
        return expiration;
    }
	
	public Claims getClaimsFromToken(String token) 
	 {
	        Claims claims;
	        try {
	            claims = Jwts.parser()
	                    .setSigningKey("secretkey")
	                    .parseClaimsJws(token)
	                    .getBody();
	        } 
	        catch (Exception e){
	            claims = null;
	        }
	        return claims;
	 }

	/**
	 * Method for creating Authentication for Spring Security Context Holder
	 * from JWT claims
	 * 
	 * @param claims
	 * @return
	 */
	public Authentication getAuthentication(Claims claims) 
	{
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		List<String> roles = (List<String>) claims.get(AUTHORITIES_KEY);
		for (String role : roles) 
		{
			authorities.add(new SimpleGrantedAuthority(role));
		}
		User principal = new User(claims.getSubject(), "", authorities);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(principal, "", authorities);
		return usernamePasswordAuthenticationToken;
	}
	
	
}
