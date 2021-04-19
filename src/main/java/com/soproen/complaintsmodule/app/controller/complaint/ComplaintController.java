package com.soproen.complaintsmodule.app.controller.complaint;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.soproen.complaintsdto.dto.BasicValidation;
import com.soproen.complaintsdto.dto.complaint.CptComplaintDTO;
import com.soproen.complaintsdto.dto.complaint.GenerateSearchComplaintsCsvFileDTO;
import com.soproen.complaintsdto.dto.complaint.RegisterNewComplaintActionDTO;
import com.soproen.complaintsdto.dto.complaint.RegisterNewComplaintForHouseholdDTO;
import com.soproen.complaintsdto.dto.complaint.SearchComplaintDTO;
import com.soproen.complaintsmodule.app.controller.AbstractParentController;
import com.soproen.complaintsmodule.app.exceptions.ServiceException;
import com.soproen.complaintsmodule.app.service.complaints.ComplaintsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/complaint")
public class ComplaintController extends AbstractParentController {

	@Autowired
	private ComplaintsService complaintsService;

	@PostMapping("/registerNewComplaintForHousehold")
	public ResponseEntity<?> registerNewComplaintForHousehold(
			@Validated(BasicValidation.class) @Valid @RequestBody RegisterNewComplaintForHouseholdDTO registerNewComplaintForHouseholdDTO,
			BindingResult bindingResult) {
		try {
			
			log.info("registerNewComplaintForHousehold {} " ,registerNewComplaintForHouseholdDTO);
			if (bindingResult.hasErrors()) {
				return new ResponseEntity<Map<String, Object>>(super.responseError("Validation Error", bindingResult),
						HttpStatus.OK);
			}
			log.info("registerNewComplaintForHousehold = {} ",registerNewComplaintForHouseholdDTO);
			CptComplaintDTO complaintDTO = utilities.mapObject(
					complaintsService.registerNewComplaintsForHousehold(registerNewComplaintForHouseholdDTO), CptComplaintDTO.class);
			
			log.info("registerNewComplaintForHousehold = {} ",complaintDTO);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", complaintDTO), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("registerNewComplaintForHousehold = {} ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError(e.getMessage()), HttpStatus.OK);
		}catch(RuntimeException e) {
			e.printStackTrace();
			log.error("registerNewComplaintForHousehold = {} ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("/findComplaintById/{idComplaint}")
	public ResponseEntity<?> findComplaintById(@PathVariable(name = "idComplaint", required = true) Long idComplaint) {
		try {

			CptComplaintDTO complaintDTO = utilities.mapObject(complaintsService.findCptComplaintById(idComplaint), CptComplaintDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", complaintDTO), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("findComplaintById = {} ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("/updateComplaint")
	public ResponseEntity<?> updateComplaint(
			@Validated(BasicValidation.class) @Valid @RequestBody CptComplaintDTO complaintDTOTmp,
			BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return new ResponseEntity<Map<String, Object>>(super.responseError("Validation Error", bindingResult),
						HttpStatus.OK);
			}
			CptComplaintDTO complaintDTO = utilities.mapObject(complaintsService.updateComplaint(complaintDTOTmp), CptComplaintDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", complaintDTO), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("updateComplaint = {} ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("/searchComplaints")
	public ResponseEntity<?> searchComplaints( @RequestBody SearchComplaintDTO searchComplaintDTO) {
		try {
			List<CptComplaintDTO> complaintsList = utilities.mapObject(
					complaintsService.searchComplaints(searchComplaintDTO),
					new TypeReference<List<CptComplaintDTO>>() {
					});
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", complaintsList), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("searchComplaints = {} ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("/registerNewComplaintAction")
	public ResponseEntity<?> registerNewComplaintAction(
			@Validated(BasicValidation.class) @Valid @RequestBody RegisterNewComplaintActionDTO registerNewComplaintActionDTO,
			BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return new ResponseEntity<Map<String, Object>>(super.responseError("Validation Error", bindingResult),
						HttpStatus.OK);
			}
			CptComplaintDTO complaintDTO = utilities.mapObject(
					complaintsService.registerNewComplaintAction(registerNewComplaintActionDTO), CptComplaintDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", complaintDTO), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("registerNewComplaintAction = {} ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	
	@PostMapping("/generateSearchComplaintCsvFile")
	public ResponseEntity<?> generateSearchComplaintCsvFile( @RequestBody SearchComplaintDTO searchComplaintDTO) {
		try {
			InputStream inputStream = complaintsService.generateSearchComplaintsCsvFile(searchComplaintDTO);
			
			byte[] targetArray = new byte[inputStream.available()];
			inputStream.read(targetArray);
			
			String encodedString = Base64.getEncoder().encodeToString(targetArray);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", GenerateSearchComplaintsCsvFileDTO.builder().encodedString(encodedString).build()), HttpStatus.OK);
		} catch (ServiceException | IOException e) {
			log.error("generateSearchComplaintCsvFile = {} ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
		

}
