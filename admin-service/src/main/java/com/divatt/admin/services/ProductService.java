package com.divatt.admin.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.divatt.admin.entity.GlobalResponse;
import com.divatt.admin.entity.ProductEntity;
import com.divatt.admin.exception.CustomException;



@Service
public class ProductService {

	
	public String productBaseUrl="localhost:8085/dev/product";
	public GlobalResponse productApproval(Integer productId, Integer designerId, String comment) {
		try
		{
			RestTemplate restTemplate= new RestTemplate();
			ResponseEntity<ProductEntity> exchange = restTemplate.exchange("http://localhost:8083/dev/product/view/"+productId, HttpMethod.GET, null ,ProductEntity.class);
			ProductEntity productdata= exchange.getBody();
			if(productdata.getDesignerId().equals(designerId))
			{
				if(productdata.getIsApprove().equals(false))
				{
					productdata.setApprovedBy("Admin");
					productdata.setApprovedOn(new Date());
					productdata.setIsApprove(true);
					productdata.setComment(comment);
					
				}
				else
				{
					productdata.setIsApprove(false);
					productdata.setComment(comment);
				}
				//Map<String, String> params= new HashMap<String,String>();
				restTemplate.put("Http://localhost:8083/dev/product/update/"+productId, productdata, String.class);
				return new GlobalResponse("Status Updated", "Product approval status changed", 200);
			}
			else
			{
				return new GlobalResponse("Bad Request", "ProductID and designerId are mismatched", 400);
			}
			//return null;
		}
		catch(Exception e)
		{
			throw new CustomException(e.getMessage());
		}
	}

}
