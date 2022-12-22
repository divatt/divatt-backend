package com.divatt.designer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.divatt.designer.config.JWTConfig;
import com.divatt.designer.constant.MessageConstant;
import com.divatt.designer.constant.RestTemplateConstant;
import com.divatt.designer.entity.account.AccountEntity;
import com.divatt.designer.entity.profile.DesignerLoginEntity;
import com.divatt.designer.exception.CustomException;
import com.divatt.designer.repo.DesignerLoginRepo;
import com.divatt.designer.response.GlobalResponce;
import com.divatt.designer.services.AccountService;

@RestController
@RequestMapping("/designerAccount")
public class AccountController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JWTConfig config;

	@Autowired
	private DesignerLoginRepo designerLoginRepo;

	@Autowired
	private AccountService accountService;

	@Value("${spring.profiles.active}")
	private String contextPath;

	@Value("${host}")
	private String host;

	@Value("${interfaceId}")
	private String interfaceId;

	@PostMapping("/add")
	public ResponseEntity<?> postAccountDetails(@Valid @RequestBody AccountEntity accountEntity,
			@RequestHeader("Authorization") String token) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inside - AccountController.postAccountDetails()");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inside - AccountController.postAccountDetails()");
		}

		try {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/add", "Success", HttpStatus.OK);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/add", "Success", HttpStatus.OK);
			}
			return accountService.postAccountDetails(accountEntity, token);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@GetMapping("/view/{accountId}")
	public ResponseEntity<?> viewAccountDetails(@PathVariable() long accountId,
			@RequestHeader("Authorization") String token) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inside - AccountController.viewAccountDetails()");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inside - AccountController.viewAccountDetails()");
		}

		try {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/view/" + accountId, "Success", HttpStatus.OK);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/view/" + accountId, "Success", HttpStatus.OK);
			}
			String extractUsername = config.extractUsername(token.substring(7));
			LOGGER.info(extractUsername);
			Optional<DesignerLoginEntity> findByEmail = designerLoginRepo.findByEmail(extractUsername);
			LOGGER.info(findByEmail + "Inside FindbyEmail");
			if (!findByEmail.isEmpty()) {
				return restTemplate.getForEntity(RestTemplateConstant.ACCOUNT_VIEW_BY_ID.getMessage() + accountId,
						AccountEntity.class);
			} else
				throw new CustomException(MessageConstant.UNAUTHORIZED.getMessage());
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/view/" + accountId, e.getLocalizedMessage(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return ResponseEntity.badRequest().body(new GlobalResponce(MessageConstant.ERROR.getMessage(),
					MessageConstant.UNAUTHORIZED.getMessage(), 400));
		}
	}

	@PutMapping("/update/{accountId}")
	public ResponseEntity<?> putAccountDetails(@Valid @RequestBody AccountEntity accountEntity,
			@PathVariable() Integer accountId, @RequestHeader("Authorization") String token) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inside - AccountController.putAccountDetails()");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inside - AccountController.putAccountDetails()");
		}

		try {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/update/" + accountId, "Success", HttpStatus.OK);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/update/" + accountId, "Success", HttpStatus.OK);
			}
			return this.accountService.putAccountDetails(accountId, accountEntity, token);
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/update/" + accountId, e.getLocalizedMessage(),
						HttpStatus.BAD_REQUEST);
			}
			throw new CustomException(e.getMessage());
		}

	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public ResponseEntity<?> getAccountDetails(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "DESC") String sort,
			@RequestParam(defaultValue = "_id") String sortName,
			@RequestParam(defaultValue = "false") Boolean isDeleted, @RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "") String designerReturn,
			@RequestParam(defaultValue = "") String serviceCharge, @RequestParam(defaultValue = "") String govtCharge,
			@RequestParam(defaultValue = "") String userOrder, @RequestParam(defaultValue = "") String ReturnStatus,
			@RequestParam Optional<String> sortBy, @RequestHeader("Authorization") String token) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inside - AccountController.getAccountDetails()");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inside - AccountController.getAccountDetails()");
		}

		try {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/list", "Success", HttpStatus.OK);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/list", "Success", HttpStatus.OK);
			}
			return this.accountService.getAccountDetails(page, limit, sort, sortName, isDeleted, keyword, designerReturn,
					serviceCharge, govtCharge, userOrder, ReturnStatus, sortBy, token);
		} catch (

		Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}", interfaceId,
						host + contextPath + "/account/list", e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
			}
			throw new CustomException(e.getMessage());
		}

	}

}