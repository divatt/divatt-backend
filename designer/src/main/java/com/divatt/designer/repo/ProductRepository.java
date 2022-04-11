package com.divatt.designer.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.divatt.designer.entity.product.ProductMasterEntity;
@Repository
public interface ProductRepository extends MongoRepository<ProductMasterEntity, Integer>{

	Optional<ProductMasterEntity> findByProductName(String productName);
	
	Optional<ProductMasterEntity>findById(Integer productId);
	
	@Query(value = "{ $or: [ { 'productId' : {$regex:?0,$options:'i'} } ] }")
	List<ProductMasterEntity>findProductData(Integer productId);
	
	Page<ProductMasterEntity> findByIsDeleted(Boolean isDeleted, Pageable pagingSort);

	@Query(value = "{ $or: [ { 'productName' : {$regex:?0,$options:'i'} }, { 'productId' : {$regex:?0,$options:'i'} },{ 'isActive' : {$regex:?0,$options:'i'} },{ 'createdOn' : {$regex:?0,$options:'i'} } ], $and: [ { 'isDeleted' : ?1 }]}")
	Page<ProductMasterEntity> Search(String sortkey, Boolean isDeleted, Pageable pagingSort);
	

}