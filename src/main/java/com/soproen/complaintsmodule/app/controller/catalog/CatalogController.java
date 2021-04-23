package com.soproen.complaintsmodule.app.controller.catalog;

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
import com.soproen.complaintsdto.dto.catalog.CptComplaintsTypeDTO;
import com.soproen.complaintsdto.dto.catalog.CptDistrictDTO;
import com.soproen.complaintsdto.dto.catalog.CptProgramDTO;
import com.soproen.complaintsdto.dto.catalog.CptSaveProgramWithComplaintsTypesDTO;
import com.soproen.complaintsdto.dto.catalog.CptTaDTO;
import com.soproen.complaintsdto.dto.catalog.CptTransferInstitutionDTO;
import com.soproen.complaintsdto.dto.catalog.CptVillageDTO;
import com.soproen.complaintsdto.dto.catalog.CptZoneDTO;
import com.soproen.complaintsdto.dto.complaint.CptComplaintsActionDTO;
import com.soproen.complaintsmodule.app.controller.AbstractParentController;
import com.soproen.complaintsmodule.app.exceptions.ServiceException;
import com.soproen.complaintsmodule.app.model.catalog.CptDistrict;
import com.soproen.complaintsmodule.app.model.catalog.CptTa;
import com.soproen.complaintsmodule.app.model.catalog.CptVillage;
import com.soproen.complaintsmodule.app.service.catalog.CatalogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/catalogs")
public class CatalogController extends AbstractParentController {

	@Autowired
	private CatalogService catalogService;

	@GetMapping("/retrieveAllPrograms")
	public ResponseEntity<?> retrieveAllPrograms() {
		try {
			List<CptProgramDTO> programList = utilities.mapObject(catalogService.retrieveAllPrograms(),
					new TypeReference<List<CptProgramDTO>>() {
					});
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", programList), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveAllPrograms = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Failed - retrieve all programs"),
					HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveAllComplaintsTypes")
	public ResponseEntity<?> retrieveAllComplaintsTypes() {
		try {
			List<CptComplaintsTypeDTO> complaintsTypeList = utilities.mapObject(
					catalogService.retrieveAllComplaintsTypes(), new TypeReference<List<CptComplaintsTypeDTO>>() {
					});
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", complaintsTypeList), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveAllComplaintsTypes = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(
					super.responseError("Failed - retrieve all complaints types"), HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveAllTransferInstitution")
	public ResponseEntity<?> retrieveAllTransferInstitution() {
		try {
			List<CptTransferInstitutionDTO> transferInstitutionList = utilities.mapObject(
					catalogService.retrieveAllTransferInstitutions(),
					new TypeReference<List<CptTransferInstitutionDTO>>() {
					});
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", transferInstitutionList),
					HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveAllTransferInstitution = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(
					super.responseError("Failed - retrieve all transfer institutions"), HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveAllDistricts")
	public ResponseEntity<?> retrieveAllDistricts() {
		try {

			List<CptDistrictDTO> districtList = utilities.mapObject(catalogService.retrieveAllDistricts(),
					new TypeReference<List<CptDistrictDTO>>() {
					});
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", districtList), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveAllDistricts = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Failed - retrieve all districts"),
					HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveAllTaByDistrict/{idDistrict}")
	public ResponseEntity<?> retrieveAllTaByDistrict(
			@PathVariable(name = "idDistrict", required = true) Long idDistrict) {
		try {
			List<CptTaDTO> taList = utilities.mapObject(
					catalogService.retrieveAllTaByDistrict(CptDistrict.builder().id(idDistrict).build()),
					new TypeReference<List<CptTaDTO>>() {
					});
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", taList), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveAllTaByDistrict = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Failed - retrieve all TA"),
					HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveAllVillagesByTa/{idTa}")
	public ResponseEntity<?> retrieveAllVillagesByTa(@PathVariable(name = "idTa", required = true) Long idTa) {
		try {
			List<CptVillageDTO> villageList = utilities.mapObject(
					catalogService.retrieveAllVillageByTa(CptTa.builder().id(idTa).build()),
					new TypeReference<List<CptVillageDTO>>() {
					});
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", villageList), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveAllVillagesByTa = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Failed - retrieve all Village"),
					HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveAllZonesByVillage/{idVillage}")
	public ResponseEntity<?> retrieveAllZonesByVillage(
			@PathVariable(name = "idVillage", required = true) Long idVillage) {
		try {
			List<CptZoneDTO> zoneList = utilities.mapObject(
					catalogService.retrieveAllZoneByVillage(CptVillage.builder().id(idVillage).build()),
					new TypeReference<List<CptZoneDTO>>() {
					});
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", zoneList), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveAllZonesByVillage = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Failed - retrieve all Zones"),
					HttpStatus.OK);
		}
	}

	@PostMapping("/saveProgramWithComplaintsTypes")
	public ResponseEntity<?> saveProgramWithComplaintsTypes(
			@Validated(BasicValidation.class) @Valid @RequestBody CptSaveProgramWithComplaintsTypesDTO cptSaveProgramWithComplaintsTypesDTO,
			BindingResult bindingResult) {
		try {

			if (bindingResult.hasErrors()) {
				return new ResponseEntity<Map<String, Object>>(super.responseError("Validation Error", bindingResult),
						HttpStatus.OK);
			}
			catalogService.saveProgramWithComplaintsTypes(cptSaveProgramWithComplaintsTypesDTO);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", null), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("saveProgramWithComplaintsTypes = {} ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveCptDistrictById/{idDistrict}")
	public ResponseEntity<?> retrieveCptDistrictById(
			@PathVariable(name = "idDistrict", required = true) Long idDistrict) {
		try {
			CptDistrictDTO district = utilities.mapObject(catalogService.findCptDistrictById(idDistrict),
					CptDistrictDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", district), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveCptDistrictById = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Retrieve District fail"),
					HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveCptTaById/{idTa}")
	public ResponseEntity<?> retrieveCptTaById(@PathVariable(name = "idTa", required = true) Long idTa) {
		try {
			CptTaDTO ta = utilities.mapObject(catalogService.findCptTaById(idTa), CptTaDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", ta), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveCptTaById = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Retrieve TA fail"), HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveCptVillageById/{idVillage}")
	public ResponseEntity<?> retrieveCptVillageById(@PathVariable(name = "idVillage", required = true) Long idVillage) {
		try {
			CptVillageDTO village = utilities.mapObject(catalogService.findCptVillageById(idVillage),
					CptVillageDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", village), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveCptVillageById = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Retrieve Village fail"), HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveCptZoneById/{idZone}")
	public ResponseEntity<?> retrieveCptZoneById(@PathVariable(name = "idZone", required = true) Long idZone) {
		try {
			CptZoneDTO village = utilities.mapObject(catalogService.findCptZoneById(idZone), CptZoneDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", village), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveCptZoneById = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Retrieve Zone fail"), HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveCptProgramById/{idProgram}")
	public ResponseEntity<?> retrieveCptProgramById(@PathVariable(name = "idProgram", required = true) Long idProgram) {
		try {
			CptProgramDTO village = utilities.mapObject(catalogService.findCptProgramById(idProgram),
					CptProgramDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", village), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveCptProgramById = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Retrieve Program fail"), HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveCptComplaintTypeById/{idComplaintType}")
	public ResponseEntity<?> retrieveCptComplaintTypeById(
			@PathVariable(name = "idComplaintType", required = true) Long idComplaintType) {
		try {
			CptComplaintsTypeDTO complaintType = utilities
					.mapObject(catalogService.findCptComplaintsTypeById(idComplaintType), CptComplaintsTypeDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", complaintType), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveCptComplaintsTypeById = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Retrieve Complaint Type fail"),
					HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveCptTransferInstitutionById/{idTransferInstitution}")
	public ResponseEntity<?> retrieveCptTransferInstitutionById(
			@PathVariable(name = "idTransferInstitution", required = true) Long idTransferInstitution) {
		try {
			CptTransferInstitutionDTO transferInstitution = utilities.mapObject(
					catalogService.findCptTransferInstitutionById(idTransferInstitution),
					CptTransferInstitutionDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", transferInstitution), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveCptTransferInstitutionById = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Retrieve Transfer Institution fail"),
					HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveAllComplaintsActions")
	public ResponseEntity<?> retrieveAllComplaintsActions() {
		try {
			List<CptComplaintsActionDTO> complaintsActionList = utilities.mapObject(catalogService.retrieveAllComplaintsActions(),
					new TypeReference<List<CptComplaintsActionDTO>>() {
					});
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", complaintsActionList), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveAllComplaintsActions = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Failed - retrieve all complaints actions"),
					HttpStatus.OK);
		}
	}

	@GetMapping("/retrieveCptComplaintActionById/{idComplaintAction}")
	public ResponseEntity<?> retrieveCptComplaintActionById(
			@PathVariable(name = "idComplaintAction", required = true) Long idComplaintAction) {
		try {
			CptComplaintsActionDTO complaintAction = utilities.mapObject(catalogService.findCptComplaintsActionById(idComplaintAction),
					CptComplaintsActionDTO.class);
			return new ResponseEntity<Map<String, Object>>(super.responseOK("OK", complaintAction), HttpStatus.OK);
		} catch (ServiceException e) {
			log.error("retrieveCptComplaintActionById = {}", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(super.responseError("Retrieve Complaint Action fail"),
					HttpStatus.OK);
		}
	}

}
