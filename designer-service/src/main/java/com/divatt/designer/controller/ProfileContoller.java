package com.divatt.designer.controller;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divatt.designer.entity.profile.DesignerLoginEntity;
import com.divatt.designer.entity.profile.DesignerProfile;
import com.divatt.designer.entity.profile.DesignerProfileEntity;
import com.divatt.designer.exception.CustomException;
import com.divatt.designer.repo.DesignerLoginRepo;
import com.divatt.designer.repo.DesignerProfileRepo;
import com.divatt.designer.response.GlobalResponce;
import com.divatt.designer.services.SequenceGenerator;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import springfox.documentation.spring.web.json.Json;


@RestController
@RequestMapping("/designer")
public class ProfileContoller {
	
	@Autowired
	private DesignerProfileRepo designerProfileRepo;
	
	@Autowired
	private DesignerLoginRepo designerLoginRepo;
	
	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getDesigner(@PathVariable Long id){
		try {
			return ResponseEntity.ok(designerProfileRepo.findById(id));
		}catch(Exception e) {
			throw new CustomException(e.getMessage());
		}
		
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addDesigner(@Valid @RequestBody DesignerProfileEntity designerProfileEntity){
		try {
			designerLoginRepo.findByEmail(designerProfileEntity.getDesignerProfile().getEmail());
			if(designerLoginRepo.findByEmail(designerProfileEntity.getDesignerProfile().getEmail()).isPresent())
				throw new CustomException("Email Already Present");
			DesignerLoginEntity designerLoginEntity = new DesignerLoginEntity();
//			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//			Date date = new Date(designerProfileEntity.getDesignerProfile().getDob());
//			String format = formatter.format(date);
//			System.out.println("format "+format);
			designerLoginEntity.setUid(sequenceGenerator.getNextSequence(DesignerLoginEntity.SEQUENCE_NAME));
			designerLoginEntity.setEmail(designerProfileEntity.getDesignerProfile().getEmail());
			designerLoginEntity.setPassword(bCryptPasswordEncoder.encode(designerProfileEntity.getDesignerProfile().getPassword()));
			designerLoginEntity.setIsActive(false);
			designerLoginEntity.setIsDeleted(false);
			designerLoginEntity.setIsApproved(false);
			designerLoginEntity.setIsProfileCompleated(false);
			designerLoginEntity.setIsProfileSubmitted(false);
			if(designerLoginRepo.save(designerLoginEntity)!=null) {
				designerProfileEntity.setDesignerId(Long.parseLong(designerLoginEntity.getUid().toString()));
				designerProfileEntity.setdId((long)sequenceGenerator.getNextSequence(DesignerProfileEntity.SEQUENCE_NAME));
				DesignerProfile designerProfile = designerProfileEntity.getDesignerProfile();
				designerProfile.setPassword(bCryptPasswordEncoder.encode(designerProfileEntity.getDesignerProfile().getPassword()));
				designerProfileEntity.setDesignerProfile(designerProfile);
				designerProfileRepo.save(designerProfileEntity);	
			}
			JsonObject jo = new JsonObject();
			jo.addProperty("senderMailId", designerProfileEntity.getDesignerProfile().getEmail());
			jo.addProperty("subject", "Successfully Registration");
			jo.addProperty("body", "Welcome " + designerProfileEntity.getDesignerProfile().getEmail() + ""
					+ ",\n                           "
					+ " you have been register successfully.Please active your account by clicking the bellow link "+ URI.create("http://localhost:8083/dev/designer/redirect/"+Base64.getEncoder().encodeToString(designerLoginEntity.getEmail().toString().getBytes())) +" . We will verify your details and come back to you soon." );
			jo.addProperty("enableHtml", false);
			try {
				Unirest.setTimeouts(0, 0);
				HttpResponse<String> response = Unirest.post("http://localhost:8080/dev/auth/sendMail")
				  .header("Content-Type", "application/json")
				  .body(jo.toString())
				  .asString();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			return ResponseEntity.ok(new GlobalResponce("SUCCESS","Register Successfully",200));
		}catch(Exception e) {
			throw new CustomException(e.getMessage());
		}
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateDesigner(@Valid @RequestBody DesignerLoginEntity designerLoginEntity){
		Optional<DesignerLoginEntity> findById = designerLoginRepo.findById(designerLoginEntity.getUid());
		if(!findById.isPresent())
			throw new CustomException("Designer Details Not Found");
		else {
			DesignerLoginEntity designerLoginEntityDB = findById.get();
			designerLoginEntityDB.setIsActive(designerLoginEntity.getIsActive());
			designerLoginEntityDB.setIsDeleted(designerLoginEntity.getIsDeleted());
			designerLoginEntityDB.setIsApproved(designerLoginEntity.getIsApproved());
			designerLoginEntityDB.setIsProfileCompleated(designerLoginEntity.getIsProfileCompleated());
			designerLoginEntityDB.setIsProfileSubmitted(designerLoginEntity.getIsProfileSubmitted());
			designerLoginRepo.save(designerLoginEntityDB);
		}
		return ResponseEntity.ok(new GlobalResponce("SUCCESS","Data Updated Successfully",200));
	}
	
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public Map<String, Object> getAll(			
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(defaultValue = "DESC") String sort, 
			@RequestParam(defaultValue = "createdOn") String sortName,
			@RequestParam(defaultValue = "false") Boolean isDeleted, 
			@RequestParam(defaultValue = "false") Boolean isApproved, 
			@RequestParam(defaultValue = "false") Boolean isProfileCompleated, 
			@RequestParam(defaultValue = "false") Boolean isProfileSubmitted,
			@RequestParam(defaultValue = "isDeleted") String fieldName,
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam Optional<String> sortBy) {

		try {		
			return this.getDesignerProfDetails(page, limit, sort, sortName, isDeleted,isApproved,isProfileCompleated,
					isProfileSubmitted,keyword,sortBy,fieldName);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}
	
	@RequestMapping(value = "/redirect/{email}", method = RequestMethod.GET)
	public void method(HttpServletResponse httpServletResponse,@PathVariable("email") String email) {
		Optional<DesignerLoginEntity> findByEmail = designerLoginRepo.findByEmail(new String(Base64.getDecoder().decode(email)));
		if(findByEmail.isPresent()) {
			DesignerLoginEntity designerLoginEntity = findByEmail.get();
			designerLoginEntity.setIsActive(true);
			designerLoginRepo.save(designerLoginEntity);
		}
			
	    httpServletResponse.setHeader("Location", "http://localhost:8083/dev/swagger-ui/index.html");
	    httpServletResponse.setStatus(302);
	}
	
	
	
	public Map<String, Object> getDesignerProfDetails(int page, int limit, String sort, String sortName, Boolean isDeleted,
			Boolean isApproved,Boolean isProfileCompleated, Boolean isProfileSubmitted, String keyword, Optional<String> sortBy,String fieldName) {
		try {
			int CountData = (int) designerLoginRepo.count();
			Pageable pagingSort = null;
			if (limit == 0) {
				limit = CountData;
			}
			
			if (sort.equals("ASC")) {
				pagingSort = PageRequest.of(page, limit, Sort.Direction.ASC, sortBy.orElse(sortName));
			} else {
				pagingSort = PageRequest.of(page, limit, Sort.Direction.DESC, sortBy.orElse(sortName));
			}

			Page<DesignerLoginEntity> findAll = null;

			if (keyword.isEmpty()) {
				
				
				if(fieldName.equals("isDeleted"))
					findAll = designerLoginRepo.findByIsDeleted(isDeleted,pagingSort);
				else if(fieldName.equals("isApproved"))
					findAll = designerLoginRepo.findByIsApproved(isApproved, pagingSort);
				else if(fieldName.equals("isProfileCompleated"))
					findAll = designerLoginRepo.findByIsProfileCompleated(isProfileCompleated, pagingSort);
				else if(fieldName.equals("isProfileSubmitted"))
					findAll = designerLoginRepo.findByIsProfileSubmitted(isProfileSubmitted, pagingSort);
				else {
					findAll = designerLoginRepo.findByIsDeleted(isDeleted,pagingSort);
				}
				

			} else {
				if(fieldName.equals("isDeleted"))
					findAll = designerLoginRepo.SearchByDelete(keyword, isDeleted, pagingSort);
				else if(fieldName.equals("isApproved"))
					findAll = designerLoginRepo.SearchByApproved(keyword, isApproved, pagingSort);
				else if(fieldName.equals("isProfileCompleated"))
					findAll = designerLoginRepo.SearchByProfileCompleated(keyword, isProfileCompleated, pagingSort);
				else if(fieldName.equals("isProfileSubmitted"))
					findAll = designerLoginRepo.SearchByProfileSubmitted(keyword, isProfileSubmitted, pagingSort);
				else {
					findAll = designerLoginRepo.SearchByDelete(keyword, isDeleted, pagingSort);
				}
			}
			
			if (findAll.getSize() <= 1) 
				throw new CustomException("Institute Not Found!");
			
			
			findAll.map(e->{
				try {
					e.setDesignerProfileEntity(designerProfileRepo.findBydesignerId(Long.parseLong(e.getUid().toString())));
				}catch(Exception o) {
					
				}
				
				return e;
			});
			
			
			int totalPage = findAll.getTotalPages() - 1;
			if (totalPage < 0) {
				totalPage = 0;
			}

			Map<String, Object> response = new HashMap<>();
			response.put("data", findAll.getContent());
			response.put("currentPage", findAll.getNumber());
			response.put("total", findAll.getTotalElements());
			response.put("totalPage", totalPage);
			response.put("perPage", findAll.getSize());
			response.put("perPageElement", findAll.getNumberOfElements());

				return response;
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

}
