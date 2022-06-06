package com.divatt.user.controller;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divatt.user.entity.OrederAndPaymentGlobalEntity;
import com.divatt.user.entity.order.OrderDetailsEntity;
import com.divatt.user.entity.orderPayment.OrderPaymentEntity;
import com.divatt.user.exception.CustomException;
import com.divatt.user.helper.JwtUtil;
import com.divatt.user.repo.OrderDetailsRepo;
import com.divatt.user.repo.UserAddressRepo;
import com.divatt.user.repo.UserLoginRepo;
import com.divatt.user.response.GlobalResponse;
import com.divatt.user.services.OrderAndPaymentService;
import com.divatt.user.services.SequenceGenerator;

@RestController
@RequestMapping("/userOrder")
public class OrderAndPaymentContoller {
	@Autowired
	private JwtUtil JwtUtil;

	@Autowired
	private OrderDetailsRepo orderDetailsRepo;

	@Autowired
	private OrderAndPaymentService orderAndPaymentService;

	@Autowired
	private SequenceGenerator sequenceGenerator;

	@Autowired
	private UserLoginRepo userLoginRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderAndPaymentContoller.class);

	@PostMapping("/razorpay/create")
	public ResponseEntity<?> postRazorpayOrderCreate(@RequestHeader("Authorization") String token,
			@Valid @RequestBody OrderDetailsEntity orderDetailsEntity) {
		LOGGER.info("Inside - OrderAndPaymentContoller.postRazorpayOrderCreate()");

		try {
				String extractUsername = null;
				try {
					extractUsername = JwtUtil.extractUsername(token.substring(7));
				} catch (Exception e) {
					throw new CustomException("Unauthorized");
				}

			if (!userLoginRepo.findByEmail(extractUsername).isPresent()) {
				throw new CustomException("Unauthorized");
			}
			return this.orderAndPaymentService.postRazorpayOrderCreateService(orderDetailsEntity);

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	@PostMapping("/payment/add")
	public void postOrderPaymentDetails(@RequestHeader("Authorization") String token,
			@Valid @RequestBody OrderPaymentEntity orderPaymentEntity) {
		LOGGER.info("Inside - OrderAndPaymentContoller.postOrderPaymentDetails()");

		try {
			String extractUsername = null;
			try {
				extractUsername = JwtUtil.extractUsername(token.substring(7));
			} catch (Exception e) {
				throw new CustomException("Unauthorized");
			}

			if (userLoginRepo.findByEmail(extractUsername).isPresent()) {
				this.orderAndPaymentService.postOrderPaymentService(orderPaymentEntity);
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	@RequestMapping(value = { "/payment/list" }, method = RequestMethod.GET)
	public Map<String, Object> getOrderPaymentDetails(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "DESC") String sort,
			@RequestParam(defaultValue = "createdOn") String sortName, @RequestParam(defaultValue = "") String keyword,
			@RequestParam Optional<String> sortBy) {
		LOGGER.info("Inside - OrderAndPaymentContoller.getOrderPaymentDetails()");

		try {
			return this.orderAndPaymentService.getOrderPaymentService(page, limit, sort, sortName, keyword, sortBy);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	@PostMapping("/add")
	public ResponseEntity<?> addOrder(@RequestHeader("Authorization") String token,
			@RequestBody OrederAndPaymentGlobalEntity orderAndPaymentGlobalEntity) {
		LOGGER.info("Inside - OrderAndPaymentContoller.addOrder()");

		try {
			Map<String, Object> map = new HashMap<>();
			String extractUsername = null;
			try {
				extractUsername = JwtUtil.extractUsername(token.substring(7));
			} catch (Exception e) {
				throw new CustomException("Unauthorized");
			}

			if (userLoginRepo.findByEmail(extractUsername).isPresent()) {

				OrderDetailsEntity orderDetailsEntity = orderAndPaymentGlobalEntity.getOrderDetailsEntity();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();
				String format = formatter.format(date);

				orderDetailsEntity.setId(sequenceGenerator.getNextSequence(OrderDetailsEntity.SEQUENCE_NAME));
				orderDetailsEntity.setOrderId("OR" + System.currentTimeMillis());
				orderDetailsEntity.setBillingAddress(orderDetailsEntity.getBillingAddress());
				orderDetailsEntity.setDiscount(orderDetailsEntity.getDiscount());
				orderDetailsEntity.setMrp(orderDetailsEntity.getMrp());
				orderDetailsEntity.setNetPrice(orderDetailsEntity.getNetPrice());
				orderDetailsEntity.setProducts(orderDetailsEntity.getProducts());
				orderDetailsEntity.setUserId(orderDetailsEntity.getUserId());
				orderDetailsEntity.setShippingAddress(orderDetailsEntity.getShippingAddress());
				orderDetailsEntity.setTaxAmount(orderDetailsEntity.getTaxAmount());
				orderDetailsEntity.setTotalAmount(orderDetailsEntity.getTotalAmount());
				orderDetailsEntity.setCreatedOn(format);
				OrderDetailsEntity OrderData = orderDetailsRepo.save(orderDetailsEntity);

				OrderPaymentEntity orderPaymentEntity = orderAndPaymentGlobalEntity.getOrderPaymentEntity();
				orderDetailsEntity.setId(sequenceGenerator.getNextSequence(OrderPaymentEntity.SEQUENCE_NAME));
				orderPaymentEntity.setOrderId(OrderData.getOrderId());
				orderPaymentEntity.setCreatedOn(new Date());
				postOrderPaymentDetails(token, orderPaymentEntity);

				map.put("orderId", OrderData.getOrderId());
				map.put("status", 200);
				map.put("message", "Order placed successfully");
			}
			return ResponseEntity.ok(map);

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public Map<String, Object> getOrderDetails(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "DESC") String sort,
			@RequestParam(defaultValue = "createdOn") String sortName, @RequestParam(defaultValue = "") String keyword,
			@RequestParam Optional<String> sortBy) {
		LOGGER.info("Inside - OrderAndPaymentContoller.getOrderDetails()");

		try {
			return this.orderAndPaymentService.getOrders(page, limit, sort, sortName, keyword, sortBy);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	@GetMapping("/getOrder/{orderId}")
	public ResponseEntity<?> getOrderDetails(@PathVariable() String orderId) {

		LOGGER.info("Inside - OrderAndPaymentContoller.getOrderDetails()");

		try {
			return this.orderAndPaymentService.getOrderDetailsService(orderId);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	@GetMapping("/getUserOrder/{userId}")
	public ResponseEntity<?> getOrderDetailsByuserId(@PathVariable() Integer userId) {

		LOGGER.info("Inside - OrderAndPaymentContoller.getOrderDetailsByuserId()");

		try {
			return this.orderAndPaymentService.getUserOrderDetailsService(userId);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	@RequestMapping(value = { "/list/{designerId}" }, method = RequestMethod.GET)
	public Map<String, Object> getOrderByDesigner(@PathVariable int designerId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(defaultValue = "DESC") String sort, @RequestParam(defaultValue = "createdOn") String sortName,
			@RequestParam(defaultValue = "") String keyword, @RequestParam Optional<String> sortBy) {
		LOGGER.info("Inside - OrderAndPaymentContoller.getOrderByDesigner()");

		try {
			return this.orderAndPaymentService.getDesigerOrders(designerId, page, limit, sort, sortName, keyword,
					sortBy);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	@GetMapping("/invoice/{orderId}")
	public GlobalResponse invoiceGenarater(@PathVariable String orderId)
	{
		try {
			return this.orderAndPaymentService.invoiceGenarator(orderId);
		}
		catch(Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
}
