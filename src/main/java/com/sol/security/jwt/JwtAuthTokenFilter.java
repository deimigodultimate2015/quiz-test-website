package com.sol.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sol.security.services.UserDetailsServiceImpl;

//Filter ... request 
public class JwtAuthTokenFilter extends OncePerRequestFilter{

	private Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);
	
	@Autowired
	private JwtProvider tokenProvider;
	
	@Autowired
	private UserDetailsServiceImpl userServiceDetails;
	
	//This method will put user detail into context for filter API
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			
			String jwt = getJwt(request);
			if(jwt != null && tokenProvider.validateJwtToken(jwt)) {
				String username = tokenProvider.getUsernameFromJwtToken(jwt);
				
				UserDetails userDetails = userServiceDetails.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
		} catch (Exception e) {
			logger.error("Can not set user authentication", e.getMessage());
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String getJwt(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.replace("Bearer ","");
		}
		
		return null;
	}
	
}
