package com.divatt.designer.services;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.divatt.designer.entity.account.AccountEntity;
import com.divatt.designer.response.GlobalResponce;



public interface AccountService {

	public ResponseEntity<?> getAccountDetails(int page, int limit, String sort, String sortName, Boolean isDeleted,
			String keyword, String designerReturn, String serviceCharge, String govtCharge, String userOrder, String ReturnStatus, Optional<String> sortBy,String token);

	public ResponseEntity<?> postAccountDetails(@Valid AccountEntity accountEntity,String token);

	public ResponseEntity<?> viewAccountDetails(long accountId,String token);

	public ResponseEntity<?> putAccountDetails(long accountId, @Valid AccountEntity accountEntity,String token);

}
