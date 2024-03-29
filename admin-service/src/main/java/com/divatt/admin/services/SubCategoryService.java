package com.divatt.admin.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.divatt.admin.entity.GlobalResponse;
import com.divatt.admin.entity.category.CategoryEntity;
import com.divatt.admin.entity.category.SubCategoryEntity;
import com.divatt.admin.exception.CustomException;
import com.divatt.admin.repo.CategoryRepo;
import com.divatt.admin.repo.SubCategoryRepo;

@Service
public class SubCategoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryService.class);

	@Autowired
	private SubCategoryRepo subCategoryRepo;

	@Autowired
	private SequenceGenerator sequenceGenerator;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	
	@Autowired
	private MongoOperations mongoOperations;

	public GlobalResponse postSubCategoryDetails(@RequestBody SubCategoryEntity subCategoryEntity) {
		LOGGER.info("Inside - SubCategoryService.postSubCategoryDetails()");

		try {
			Query query= new Query();
			query.addCriteria(Criteria.where("parentId").is(subCategoryEntity.getParentId()).and("categoryName").is(subCategoryEntity.getCategoryName()));
			List<SubCategoryEntity> findBySubCategoryName=mongoOperations.find(query, SubCategoryEntity.class);
			if (findBySubCategoryName.size() >= 1) {
				throw new CustomException("Subcategory already exist!");
			} else {
				SubCategoryEntity filterSubCatDetails = new SubCategoryEntity();

				filterSubCatDetails.setId(sequenceGenerator.getNextSequence(SubCategoryEntity.SEQUENCE_NAME));
				filterSubCatDetails.setCategoryName(subCategoryEntity.getCategoryName());
				filterSubCatDetails.setCategoryDescription(subCategoryEntity.getCategoryDescription());
				filterSubCatDetails.setCategoryImage(subCategoryEntity.getCategoryImage());
				filterSubCatDetails.setCreatedBy(subCategoryEntity.getCreatedBy());
				filterSubCatDetails.setCreatedOn(new Date());
				filterSubCatDetails.setLevel(subCategoryEntity.getLevel());
				filterSubCatDetails.setParentId(subCategoryEntity.getParentId());
				filterSubCatDetails.setIsActive(true);
				filterSubCatDetails.setIsDeleted(false);

				subCategoryRepo.save(filterSubCatDetails);
				return new GlobalResponse("SUCCESS", "Subcategory added successfully", 200);

			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	public Map<String, Object> getSubCategoryDetails(int page, int limit, String sort, String sortName,
			Boolean isDeleted, String keyword, Optional<String> sortBy) {
		try {
			int CountData = (int) subCategoryRepo.count();
			Pageable pagingSort = null;
			if (limit == 0) {
				limit = CountData;
			}

			if (sort.equals("ASC")) {
				pagingSort = PageRequest.of(page, limit, Sort.Direction.ASC, sortBy.orElse(sortName));
			} else {
				pagingSort = PageRequest.of(page, limit, Sort.Direction.DESC, sortBy.orElse(sortName));
			}

			Page<SubCategoryEntity> findAll = null;

			if (keyword.isEmpty()) {
//				findAll = subCategoryRepo.findByIsDeleted(isDeleted,pagingSort);
				findAll = subCategoryRepo.findByIsDeletedAndParentIdNot(isDeleted, "0", pagingSort);

			} else {
				findAll = subCategoryRepo.SearchAndfindByIsDeletedAndParentIdNot(keyword, isDeleted, "0", pagingSort);

			}

//			return ResponseEntity.ok(subCategoryRepo.findAll().stream()
//										.filter(e->!e.getParentId().equals("0"))
//										.map(e->{ e.setParentId(subCategoryRepo.findById(Integer.parseInt(e.getParentId())).get().getCategoryName());return e;}).toList());

//				List<SubCategoryEntity> lists = findAll.getContent().stream()
//					.filter(e->!e.getParentId().equals("0"))
//					.map(e->{
//						SubCategoryEntity subCategoryEntity = subCategoryRepo.findById(Integer.parseInt(e.getParentId())).get();
//						subCategoryEntity.setSubCategory(e);
//						return subCategoryEntity;
//				}).collect(Collectors.toList());
//				 Page<SubCategoryEntity> list=new Page<>(page,limit,lists);

			Page<SubCategoryEntity> map = findAll.map(e -> {
				try {
					SubCategoryEntity subCategoryEntity = subCategoryRepo.findById(Integer.parseInt(e.getParentId()))
							.get();
					subCategoryEntity.setSubCategory(e);
					return subCategoryEntity;
				} catch (Exception z) {
					return e;
				}

			});

			int totalPage = findAll.getTotalPages() - 1;
			if (totalPage < 0) {
				totalPage = 0;
			}

			Map<String, Object> response = new HashMap<>();
			response.put("data", map.getContent());
			response.put("currentPage", map.getNumber());
			response.put("total", map.getTotalElements());
			response.put("totalPage", totalPage);
			response.put("perPage", map.getSize());
			response.put("perPageElement", map.getNumberOfElements());

			if (findAll.getSize() <= 1) {
				throw new CustomException("Subcategory not found!");
			} else {
				return response;
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	public Optional<SubCategoryEntity> viewSubCategoryDetails(Integer catId) {
		try {
			Optional<SubCategoryEntity> findById = this.subCategoryRepo.findById(catId);

			if (!(findById.isPresent())) {
				throw new CustomException("Subcategory not found!");
			} else {
				return findById;
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	public GlobalResponse putSubCategoryDetailsService(SubCategoryEntity subCategoryEntity, Integer catId) {

		try {
			Optional<SubCategoryEntity> findByCategoryRow = subCategoryRepo.findById(catId);

			if (!findByCategoryRow.isPresent()) {
				throw new CustomException("Subcategory not exist!");
			} else {
				SubCategoryEntity filterSubCatDetails = findByCategoryRow.get();

				filterSubCatDetails.setCategoryName(subCategoryEntity.getCategoryName());
				filterSubCatDetails.setCategoryDescription(subCategoryEntity.getCategoryDescription());
				filterSubCatDetails.setCategoryImage(subCategoryEntity.getCategoryImage());
				filterSubCatDetails.setCreatedBy(subCategoryEntity.getCreatedBy());
				filterSubCatDetails.setCreatedOn(new Date());
				filterSubCatDetails.setLevel(subCategoryEntity.getLevel());
				filterSubCatDetails.setParentId(subCategoryEntity.getParentId());
				filterSubCatDetails.setIsActive(true);
				filterSubCatDetails.setIsDeleted(false);
				subCategoryRepo.save(filterSubCatDetails);

				return new GlobalResponse("SUCCESS", "Subcategory updated succesfully", 200);
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	public GlobalResponse putSubCategoryDeleteService(Integer CatId) {
		try {
			Optional<SubCategoryEntity> findById = subCategoryRepo.findById(CatId);
			SubCategoryEntity filterSubCatDetails = findById.get();
			if (!findById.isPresent()) {
				throw new CustomException("Subcategory not exist!");
			} else {
				filterSubCatDetails.setIsDeleted(true);
				filterSubCatDetails.setCreatedOn(new Date());
				subCategoryRepo.save(filterSubCatDetails);

				return new GlobalResponse("SUCCESS", "Subcategory deleted successfully", 200);
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	public GlobalResponse putSubCategoryStatusService(Integer CatId) {
		try {

			Optional<SubCategoryEntity> findById = subCategoryRepo.findById(CatId);
			SubCategoryEntity filterSubCatDetails = findById.get();

			if (filterSubCatDetails.getId() == null) {
				throw new CustomException("Subcategory not exist!");
			} else {
				Boolean isStatus = null;
				String message = null;
				if (filterSubCatDetails.getIsActive() == false) {
					isStatus = true;
					message = "actived";
				} else {
					isStatus = false;
					message = "inactive";
				}

				filterSubCatDetails.setIsActive(isStatus);
				filterSubCatDetails.setCreatedOn(new Date());
				subCategoryRepo.save(filterSubCatDetails);

				return new GlobalResponse("SUCCESS", "Status " + message + " successfully", 200);
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	public GlobalResponse putSubCategoryMulDeleteService(List<Integer> cateID) {
		try {
			for (Integer CateIdRowId : cateID) {

				Optional<SubCategoryEntity> findById = subCategoryRepo.findById(CateIdRowId);
				SubCategoryEntity filterCatDetails = findById.get();

				if (filterCatDetails.getId() != null) {
					filterCatDetails.setIsDeleted(true);
					filterCatDetails.setCreatedOn(new Date());
					subCategoryRepo.save(filterCatDetails);
				}
			}
			return new GlobalResponse("SUCCESS", "Subcategory deleted successfully", 200);

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	public List<Integer> subcategoryVerification(List<Integer> subCategoryId) {
		try {
			List<Integer> subCategoryList = new ArrayList<Integer>();
			for (int i = 0; i < subCategoryId.size(); i++) {
				if (subCategoryRepo.existsById(subCategoryId.get(i))) {
					SubCategoryEntity subCategoryData = viewSubCategoryDetails(subCategoryId.get(i)).get();
					if (subCategoryData.getIsActive().equals(true)) {
						subCategoryList.add(subCategoryId.get(i));

					}
				}
			}
			return subCategoryList;
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	public List<SubCategoryEntity> getAllSubCategoryDetails(String catId, Boolean isDeleted, Boolean Status) {
		try {

			List<SubCategoryEntity> findAll = subCategoryRepo
					.findByParentIdAndIsDeletedAndIsActive(catId,
					isDeleted, Status);

			if (findAll.isEmpty()) {
				throw new CustomException("Subcategory not found!");
			} else {
				return findAll;
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}
}
