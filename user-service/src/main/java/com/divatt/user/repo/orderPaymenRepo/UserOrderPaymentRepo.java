package com.divatt.user.repo.orderPaymenRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.divatt.user.entity.order.OrderDetailsEntity;
import com.divatt.user.entity.orderPayment.OrderPaymentEntity;



public interface UserOrderPaymentRepo extends MongoRepository<OrderPaymentEntity, Integer> {

//	Optional<UserOrderPaymentRepo> findByProductIdAndUserId(Integer ProductId,Integer UserId);
	
	@Query(value = "{ $or: [ { 'order_id' : {$regex:?0,$options:'i'} }, { 'user_id' : {$regex:?0,$options:'i'} } ]}")
	Page<OrderPaymentEntity> Search(String sortKey, Pageable pageable);
	
	Optional<OrderPaymentEntity> findByOrderId(String orderId);
	
	Optional<OrderPaymentEntity> findByUserId(Integer userId);
	
	
	@Query(value = "{ 'payment_details.razorpay_payment_id' : {$regex:?0,$options:'i'} }, { 'payment_details.razorpay_order_id' : {$regex:?0,$options:'i'}}")
	Optional<OrderPaymentEntity> findPaymentId(String PayID,String OrID);
	
	
}
