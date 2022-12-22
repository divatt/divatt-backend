package com.divatt.admin.servicesImpl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.divatt.admin.constant.MessageConstant;
import com.divatt.admin.entity.AccountEntity;
import com.divatt.admin.entity.AccountMapEntity;
import com.divatt.admin.entity.GlobalResponse;
import com.divatt.admin.exception.CustomException;
import com.divatt.admin.repo.AccountRepo;
import com.divatt.admin.repo.AccountTemplateRepo;
import com.divatt.admin.services.AccountService;
import com.divatt.admin.services.SequenceGenerator;
import com.google.gson.Gson;

@Service
public class AccountServiceImpl implements AccountService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private AccountTemplateRepo accountTemplateRepo;

	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@Value("${spring.profiles.active}")
	private String contextPath;

	@Value("${host}")
	private String host;
	
	@Value("${interfaceId}")
	private String interfaceId;
	
	@Autowired
	private Gson gson;

	public GlobalResponse postAccountDetails(@RequestBody AccountEntity accountEntity) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inside - AccountServiceImpl.postAccountDetails()");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inside - AccountServiceImpl.postAccountDetails()");
		}

		try {

//			List<AccountEntity> findByRow = accountRepo.findByOrderIdAndInvoiceId(accountEntity.getOrder_details().get(0).getOrder_id(), accountEntity.getOrder_details().get(0).getInvoice_id());
//
//			if (findByRow.size() > 0) {
//				if (LOGGER.isErrorEnabled()) {
//					LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}",
//							interfaceId, host + contextPath + "/account/add", MessageConstant.ACCOUNT_NOT_FOUND.getMessage(),
//							HttpStatus.BAD_REQUEST);
//				}
//				throw new CustomException(MessageConstant.ORDER_ALREADY_EXIST.getMessage());
//			}
				
				long currentTimeMillis = System.currentTimeMillis();
			
				accountEntity.getService_charge().setDesigner_invoice_id("INV"+currentTimeMillis);
				accountEntity.getGovt_charge().get(0).setDesigner_invoice_id("INV"+currentTimeMillis);
				accountEntity.setId(sequenceGenerator.getNextSequence(AccountEntity.SEQUENCE_NAME));
				accountRepo.save(accountEntity);

				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/add", "Success", HttpStatus.OK);
				}
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/add", gson.toJson(accountEntity), HttpStatus.OK);
				}
				return new GlobalResponse(MessageConstant.SUCCESS.getMessage(),
						MessageConstant.ACCOUNT_ADDED.getMessage(), HttpStatus.OK.value());
			

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}",
						interfaceId, host + contextPath + "/account/list", e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
			}
			throw new CustomException(e.getMessage());
		}

	}
	
	public ResponseEntity<?> viewAccountDetails(long accountId) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inside - AccountServiceImpl.viewAccountDetails()");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inside - AccountServiceImpl.viewAccountDetails()");
		}

		try {
            List<AccountEntity> findByRow = accountRepo.findById(accountId);
			if (findByRow.size() < 0) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/view/"+accountId, MessageConstant.ACCOUNT_NOT_FOUND.getMessage(),
							HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<>(MessageConstant.ACCOUNT_NOT_FOUND.getMessage(),HttpStatus.BAD_REQUEST);
			}
				
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/view/"+accountId, "Success", HttpStatus.OK);
				}
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/view/"+accountId, gson.toJson(findByRow), HttpStatus.OK);
				}
				return new ResponseEntity<>(findByRow.get(0), HttpStatus.OK);

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}",
						interfaceId, host + contextPath + "/account/view/"+accountId, e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	public GlobalResponse putAccountDetails(long accountId, @RequestBody AccountEntity accountEntity) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inside - AccountServiceImpl.putAccountDetails()");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inside - AccountServiceImpl.putAccountDetails()");
		}

		try {

			Optional<AccountEntity> findByRow = accountRepo.findByAccountId(accountId);

			if (!findByRow.isPresent()) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/update/"+accountId, MessageConstant.ACCOUNT_NOT_FOUND.getMessage(),
							HttpStatus.BAD_REQUEST);
				}
				throw new CustomException(MessageConstant.ACCOUNT_NOT_FOUND.getMessage());
			}

			accountTemplateRepo.update(accountId, accountEntity);

				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/update/"+accountId, "Success", HttpStatus.OK);
				}
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/update/"+accountId, gson.toJson(accountEntity), HttpStatus.OK);
				}
				return new GlobalResponse(MessageConstant.SUCCESS.getMessage(), MessageConstant.ACCOUNT_UPDATED.getMessage(), HttpStatus.OK.value());
			

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}",
						interfaceId, host + contextPath + "/account/update/"+accountId, e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
			}
			throw new CustomException(e.getMessage());
		}

	}

	public Map<String, Object> getAccountDetails(int page, int limit, String sort, String sortName, Boolean isDeleted,
			String keyword, String designerReturn, String serviceCharge, String govtCharge, String userOrder, String ReturnStatus, 
			String settlement, int year, int month, Optional<String> sortBy) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inside - AccountServiceImpl.getAccountDetails()");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inside - AccountServiceImpl.getAccountDetails()");
		}
		try {
			Pageable pagingSort = null;

			if (sort.equals("ASC")) {
				pagingSort = PageRequest.of(page, limit, Sort.Direction.ASC, sortBy.orElse(sortName));
			} else {
				pagingSort = PageRequest.of(page, limit, Sort.Direction.DESC, sortBy.orElse(sortName));
			}

			Page<AccountEntity> findAll = null;
			List<AccountMapEntity> getServiceFee = accountTemplateRepo.getServiceFee(settlement, year, month);
			List<AccountMapEntity> getBasicAmount = accountTemplateRepo.getBasicAmount(settlement, year, month);
			List<AccountMapEntity> getDesignerGstAmount = accountTemplateRepo.getDesignerGstAmount(settlement, year, month);
			List<AccountMapEntity> getGovtGstAmount = accountTemplateRepo.getGovtChargeAmount(settlement, year, month);
			List<AccountMapEntity> getGstAmount = accountTemplateRepo.getGstAmount(settlement, year, month);
			List<AccountMapEntity> getPayableAmount = accountTemplateRepo.getPayableAmount(settlement, year, month);
			List<AccountMapEntity> getPendingAmount = accountTemplateRepo.getPendingAmount(settlement, year, month);
			List<AccountMapEntity> getTotalTcs = accountTemplateRepo.getTcsAmount(settlement, year, month);
			List<AccountMapEntity> getTotalAmount = accountTemplateRepo.getTotalAmount(settlement, year, month);
			
			if (keyword.isEmpty()) {
//				findAll = accountRepo.findAllByOrderByIdDesc(pagingSort);
				findAll = accountTemplateRepo.getAccountData(designerReturn, serviceCharge, govtCharge, userOrder, ReturnStatus, 
						settlement, year, month, pagingSort);
			} else {
				findAll = accountTemplateRepo.AccountSearchByKeywords(keyword, pagingSort);
//				findAll = accountRepo.AccountSearchByKeywords(keyword, pagingSort);
			}

			int totalPage = findAll.getTotalPages() - 1;
			if (totalPage < 0) {
				totalPage = 0;
			}
			final DecimalFormat df = new DecimalFormat("0.00");
			double totalServiceFee = 0.00;
			double basicAmount = 0.00;
			double gstAmount = 0.00;
			double totalAmount = 0.00;
			double totalTcs = 0.00;
			double pendingAmount = 0.00;
			double govtGstAmount = 0.00;
			double payableAmount = 0.00;
			double designerGstAmount = 0.00;
			
			if(getServiceFee.size() > 0) {
				totalServiceFee = getServiceFee.get(0).getServiceFee();
			}
			if(getBasicAmount.size() > 0) {
				basicAmount = getBasicAmount.get(0).getBasicAmount();
			}
			if(getDesignerGstAmount.size() > 0) {
				designerGstAmount = getDesignerGstAmount.get(0).getDesignerGstAmount();
			}
			if(getGovtGstAmount.size() > 0) {
				govtGstAmount = getGovtGstAmount.get(0).getGovtGstAmount();
			}
			if(getGstAmount.size() > 0) {
				gstAmount = getGstAmount.get(0).getGstAmount();
			}
			if(getPayableAmount.size() > 0) {
				payableAmount = getPayableAmount.get(0).getPayableAmount();
			}
			if(getPendingAmount.size() > 0) {
				pendingAmount = getPendingAmount.get(0).getPendingAmount();
			}
			if(getTotalTcs.size() > 0) {
				totalTcs = getTotalTcs.get(0).getTcs();
			}
			if(getTotalAmount.size() > 0) {
				totalAmount = getTotalAmount.get(0).getTotalAmount();
			}
			
			Map<String, Object> response = new HashMap<>();
			response.put("data", findAll.getContent());
			response.put("currentPage", findAll.getNumber());
			response.put("total", findAll.getTotalElements());
			response.put("totalPage", totalPage);
			response.put("perPage", findAll.getSize());
			response.put("perPageElement", findAll.getNumberOfElements());
			response.put("totalServiceFee", Double.valueOf(df.format(totalServiceFee)));
			response.put("basicAmount", Double.valueOf(df.format(basicAmount)));
			response.put("designerGstAmount", Double.valueOf(df.format(designerGstAmount)));
			response.put("govtGstAmount", Double.valueOf(df.format(govtGstAmount)));
			response.put("gstAmount", Double.valueOf(df.format(gstAmount)));
			response.put("payableAmount", Double.valueOf(df.format(payableAmount)));
			response.put("totalAmount", Double.valueOf(df.format(totalAmount)));
			response.put("totalTcs", Double.valueOf(df.format(totalTcs)));
			response.put("pendingAmount", Double.valueOf(df.format(pendingAmount)));

			if (findAll.getSize() < 1) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/list", MessageConstant.ACCOUNT_NOT_FOUND.getMessage(),
							HttpStatus.BAD_REQUEST);
				}
				throw new CustomException(MessageConstant.ACCOUNT_NOT_FOUND.getMessage());
			} else {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/list", "Success", HttpStatus.OK);
				}
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Application name: {},Request URL: {},Response message: {},Response code: {}",
							interfaceId, host + contextPath + "/account/list", gson.toJson(response), HttpStatus.OK);
				}
				return response;
			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Application name: {},Request URL: {},Response message: {},Response code: {}",
						interfaceId, host + contextPath + "/account/list", e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
			}
			throw new CustomException(e.getMessage());
		}

	}

}