package kr.co.seoulit.erp.logistic.production.controller;

import java.util.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import kr.co.seoulit.erp.logistic.production.servicefacade.MrpServiceFacade;
import kr.co.seoulit.erp.logistic.production.to.MpsTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import kr.co.seoulit.erp.logistic.production.to.MrpTO;

import static kr.co.seoulit.erp.sys.to.response.Response.success;

@Tag(name = "소요량전개(MRP)")
@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/logi/logistics/production/*", produces = "application/json")
public class MrpController {

	private final MrpServiceFacade mrpSF;
	@Autowired
	public MrpController(MrpServiceFacade mrpSF) {
		this.mrpSF = mrpSF;
	}

	/*****************************
			 MPS 테이블 조회
	 *****************************/
	@Operation(summary = "MPS 테이블 조회")
	@RequestMapping("/searchMpsInfo")
	public Map<String, List<MpsTO>> getMpsList(@RequestParam String startDate,
											   @RequestParam String endDate,
											   @RequestParam String classification) {

		Map<String, List<MpsTO>> mpsList = mrpSF.getMpsList(startDate, endDate, classification);
		return mpsList;
	}

	/*****************************
			    MPS 수정
	 *****************************/
	@Operation(summary = "MPS 수정")
	@RequestMapping(value="/updateMps", method = RequestMethod.POST)
	public void updateMps(@RequestBody MpsTO mpsTO) {
		mrpSF.updateMps(mpsTO);
	}

	/*****************************
	          MRP 모의전개
	 *****************************/
	@Operation(summary = "MRP 모의전개")
	@RequestMapping("/openMrp")
	public HashMap<String, Object> openMrp(@RequestParam String mpsNo) {
		HashMap<String, Object> map = mrpSF.openMrp(mpsNo);
		return map;
	}

	/*****************************
	 			MRP 등록
	 *****************************/
	@Operation(summary = "MRP 등록")
	@RequestMapping(value = "/registerMrp", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<HashMap<String, Object>> registerMrp(@RequestBody Map<String, Object> params) {

		String mrpRegisterDate = params.get("mrpRegisterDate").toString();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ArrayList<MrpTO> newMrpList = mapper.convertValue(params.get("batchList"),
				TypeFactory.defaultInstance().constructCollectionType(List.class, MrpTO.class));
		HashMap<String, Object> resultMap = mrpSF.registerMrp(mrpRegisterDate, newMrpList);

		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}

}
