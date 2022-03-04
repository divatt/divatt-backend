package com.ecom.auth.controller;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.auth.entity.LoginEntity;
import com.ecom.auth.entity.LoginUserData;
import com.ecom.auth.exception.CustomException;
import com.ecom.auth.helper.JwtUtil;
import com.ecom.auth.repo.LoginRepository;
import com.ecom.auth.services.LoginService;
import com.ecom.auth.services.LoginUserDetails;

@RestController
@SuppressWarnings("All")
@RequestMapping("/auth")

public class EcomAuthController {
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private LoginUserDetails loginUserDetails;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private LoginService loginService;
	
	@PostMapping("/login")
	public ResponseEntity<?> superAdminLogin(@RequestBody LoginEntity loginEntity) {
		
	Logger LOGGER = LoggerFactory.getLogger(EcomAuthController.class);
		
		LOGGER.info("Inside - LoginContoller.superAdminLogin()");
		
		try {
			try {
				this.authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginEntity.getUserName(), loginEntity.getPassword()));
			} catch (Exception e) {
				throw new CustomException(e.getMessage());
			}

			UserDetails vendor = this.loginUserDetails.loadUserByUsername(loginEntity.getUserName());

			String token = jwtUtil.generateToken(vendor);

			Optional<LoginEntity> findByUserName = loginRepository.findByUserName(vendor.getUsername());
			
			LoginEntity loginEntityAfterCheck = findByUserName.get();
			loginEntityAfterCheck.setAccessToken(token);
			LoginEntity save = loginRepository.save(loginEntityAfterCheck);
			
			if(save.equals(null)) {
				throw new CustomException("Data Not Save Try Again");
			}
//			HttpHeaders responseHeaders = new HttpHeaders();
//		    responseHeaders.set("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

			return ResponseEntity.ok(new LoginUserData(token,findByUserName.get().getUid(),findByUserName.get().getUserName() , findByUserName.get().getPassword(), "Login successful", 200));
		
		}catch(Exception e) {
			throw new CustomException(e.getMessage());
		}
	}	
	
}
