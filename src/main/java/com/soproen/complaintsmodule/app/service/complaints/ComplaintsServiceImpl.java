package com.soproen.complaintsmodule.app.service.complaints;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soproen.complaintsdto.dto.complaint.CptComplaintDTO;
import com.soproen.complaintsdto.dto.complaint.RegisterNewComplaintActionDTO;
import com.soproen.complaintsdto.dto.complaint.RegisterNewComplaintForHouseholdDTO;
import com.soproen.complaintsdto.dto.complaint.SearchComplaintDTO;
import com.soproen.complaintsmodule.app.enums.CptComplaintsActionResultEnum;
import com.soproen.complaintsmodule.app.enums.CptComplaintsStatusEnum;
import com.soproen.complaintsmodule.app.enums.RequiredComplaintFiledsEnum;
import com.soproen.complaintsmodule.app.exceptions.ServiceException;
import com.soproen.complaintsmodule.app.model.catalog.CptComplaintsAction;
import com.soproen.complaintsmodule.app.model.catalog.CptDistrict;
import com.soproen.complaintsmodule.app.model.catalog.CptProgram;
import com.soproen.complaintsmodule.app.model.catalog.CptTa;
import com.soproen.complaintsmodule.app.model.catalog.CptTransferInstitution;
import com.soproen.complaintsmodule.app.model.catalog.CptVillage;
import com.soproen.complaintsmodule.app.model.catalog.CptZone;
import com.soproen.complaintsmodule.app.model.complaints.CptComplaint;
import com.soproen.complaintsmodule.app.model.complaints.CptComplaintsActionRegistry;
import com.soproen.complaintsmodule.app.model.complaints.CptComplaintsStatus;
import com.soproen.complaintsmodule.app.model.complaints.CptHouseholdComplaint;
import com.soproen.complaintsmodule.app.model.complaints.CptHouseholdMemberComplaints;
import com.soproen.complaintsmodule.app.repository.complaints.CptComplaintRepository;
import com.soproen.complaintsmodule.app.repository.dynamicsearch.ComplaintsSpecs;
import com.soproen.complaintsmodule.app.service.catalog.CatalogService;
import com.soproen.complaintsmodule.app.utilities.CsvUtils;
import com.soproen.complaintsmodule.app.utilities.Utilities;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ComplaintsServiceImpl implements ComplaintsService {

	@Autowired
	protected Utilities utilities;
	@Autowired
	private CptComplaintRepository complaintRepository;
	@Autowired
	private Environment env;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private CsvUtils csvUtils;

	@Override
	@Transactional
	public CptComplaint registerNewComplaintsForHousehold(
			RegisterNewComplaintForHouseholdDTO registerNewComplaintForHouseholdDTO) throws ServiceException {
		try {

			Date currentDate = Calendar.getInstance().getTime();

			CptHouseholdComplaint householdComplaint = utilities.mapObject(
					registerNewComplaintForHouseholdDTO.getCptHouseholdComplaint(), CptHouseholdComplaint.class);

			householdComplaint.getCptHouseholdMembersComplaints()
					.forEach(obj -> obj.setIsPresentedComplaint(Boolean.FALSE));

			CptComplaint newComplaintTmp = CptComplaint.builder().complaintsNumber("Comp-" + currentDate.getTime())
					.createdBy(registerNewComplaintForHouseholdDTO.getUsernameCreatedBy())
					.cptHouseholdsComplaint(Arrays.asList(householdComplaint))
					.cptComplaintsStatuses(Arrays.asList(CptComplaintsStatus.builder().createdAt(currentDate)
							.status(CptComplaintsStatusEnum.INCOMPLETE)
							.usernameCreatedBy(registerNewComplaintForHouseholdDTO.getUsernameCreatedBy()).build()))
					.build();

			CptComplaint newCptComplaint = complaintRepository.save(newComplaintTmp);
			String complaintNumber = "Comp-" + newCptComplaint.getId();
			complaintRepository.updateComplaintNumber(complaintNumber, newCptComplaint.getId());
			return newCptComplaint;

		} catch (DataAccessException e) {
			log.error("registerNewComplaintsForHousehold = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("registerNewComplaintsForHousehold = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CptComplaint findCptComplaintById(Long idCptComplaint) throws ServiceException {
		try {

			CptComplaint newComplaint = complaintRepository.findById(idCptComplaint)
					.orElseThrow(() -> new ServiceException("Complaint not found"));

			prepareteCptComplaintToReturn(newComplaint);

			return newComplaint;

		} catch (DataAccessException e) {
			log.error("findCptComplaintById = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	private void prepareteCptComplaintToReturn(CptComplaint newComplaint) {

		List<CptComplaintsStatus> cptComplaintsStatusesList = newComplaint.getCptComplaintsStatuses().stream()
				.filter(obj -> obj.getClosedAt() == null).collect(Collectors.toList());
		newComplaint.setCptComplaintsStatuses(cptComplaintsStatusesList);

		newComplaint.getCptHouseholdsComplaint().iterator().next().getCptHouseholdMembersComplaints().stream()
				.forEach(obj -> {
					if (obj.getIsPresentedComplaint() != null && obj.getIsPresentedComplaint()) {
						newComplaint.setSelectedMemberHowPresentComplaints(obj);
					}
				});

	}

	@Override
	@Transactional
	public CptComplaint updateComplaint(CptComplaintDTO cptComplaintDTO) throws ServiceException {
		try {

			Date currentDate = Calendar.getInstance().getTime();

			CptComplaint cptComplaint = utilities.mapObject(cptComplaintDTO, CptComplaint.class);
			log.info(" complaint = {}", cptComplaint);

			if (cptComplaint.getSelectedMemberHowPresentComplaints() != null
					&& cptComplaint.getSelectedMemberHowPresentComplaints().getId() != null) {
				final Long idSelectedMemberHowPresentComplaint = cptComplaint.getSelectedMemberHowPresentComplaints()
						.getId();
				cptComplaint.getCptHouseholdsComplaint().iterator().next().getCptHouseholdMembersComplaints().stream()
						.forEach(obj -> {
							obj.setIsPresentedComplaint(Boolean.FALSE);
							if (obj.getId().intValue() == idSelectedMemberHowPresentComplaint.intValue()) {
								obj.setIsPresentedComplaint(Boolean.TRUE);
							}
						});
			}

			// close current status
			cptComplaint.setCptComplaintsStatuses(complaintRepository.findById(cptComplaintDTO.getId())
					.orElseThrow(() -> new ServiceException("Complaint not found")).getCptComplaintsStatuses());
			cptComplaint.getCptComplaintsStatuses().stream().forEach(complaintStatusesTmp -> {
				if (complaintStatusesTmp.getClosedAt() == null) {
					complaintStatusesTmp.setClosedAt(currentDate);
				}
			});

			log.info(" complaint = {}", cptComplaint);
			Map<RequiredComplaintFiledsEnum, String> mapResult = isComplaintFormComplete(cptComplaint);
			for (Map.Entry<RequiredComplaintFiledsEnum, String> obj : mapResult.entrySet()) {
				log.info("key = {}, value = {}", obj.getKey(), obj.getValue());
			}
			cptComplaint.getCptComplaintsStatuses().add(CptComplaintsStatus.builder().createdAt(currentDate)
					.status(mapResult.isEmpty() ? CptComplaintsStatusEnum.OPEN : CptComplaintsStatusEnum.INCOMPLETE)
					.createdAt(currentDate).usernameCreatedBy(cptComplaint.getApplicationUserName()).build());

			cptComplaint = complaintRepository.save(cptComplaint);
			cptComplaint.setMapResultComplaintFieldsValidation(mapResult);
			return cptComplaint;

		} catch (DataAccessException e) {
			log.error("updateComplaint = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	private Map<RequiredComplaintFiledsEnum, String> isComplaintFormComplete(CptComplaint cptComplaint) {

		Map<RequiredComplaintFiledsEnum, String> mapResult = new HashMap<>();

		if (!cptComplaint.getCptHouseholdsComplaint().iterator().next().getCptHouseholdMembersComplaints().stream()
				.filter(obj -> obj.getIsPresentedComplaint() != null && obj.getIsPresentedComplaint()).findAny()
				.isPresent()) {
			mapResult.put(RequiredComplaintFiledsEnum.MEMBER_HOW_PRESENT_COMPLAINT,
					env.getProperty("app.complaint-fields.member-how-present-complaint"));
		}

		if (cptComplaint.getCptTransferInstitution() == null
				|| cptComplaint.getCptTransferInstitution().getId() == null) {
			mapResult.put(RequiredComplaintFiledsEnum.TRANSFER_INSTITUTION,
					env.getProperty("app.complaint-fields.transfer-institution"));
		}

		if (cptComplaint.getCptComplaintType() == null || cptComplaint.getCptComplaintType().getId() == null) {
			mapResult.put(RequiredComplaintFiledsEnum.TYPE_OF_COMPLAINT,
					env.getProperty("app.complaint-fields.complaint-type"));
		}

		if (cptComplaint.getOfficerName() == null || cptComplaint.getOfficerName().isEmpty()) {
			mapResult.put(RequiredComplaintFiledsEnum.NAME_OFFICER,
					env.getProperty("app.complaint-fields.officer-name"));
		}

		if (cptComplaint.getCreatedAt() == null) {
			mapResult.put(RequiredComplaintFiledsEnum.COMPLAINT_DATE,
					env.getProperty("app.complaint-fields.complaint-date"));
		}

		return mapResult;

	}

	@Override
	@Transactional(readOnly = true)
	public InputStream generateSearchComplaintsCsvFile(SearchComplaintDTO searchComplaintDTO) throws ServiceException {
		try {

			List<CptComplaint> list = searchComplaints(searchComplaintDTO);
			List<Object[]> dataList = list.stream().map(tmp -> {
				return composeDataForComplaintExportedFile(tmp);
			}).collect(Collectors.toList());

			InputStream newInputSream = csvUtils.createCsvFile(dataList, new String[] { "" });

			return newInputSream;

		} catch (DataAccessException e) {
			log.error("generateSearchComplaintsCsvFile = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	private Object[] composeDataForComplaintExportedFile(CptComplaint tmp) {

		String selectedMemberFistName = "";
		String selectedMemberLastName = "";
		Optional<CptHouseholdMemberComplaints> optionalSelectedMember = tmp.getCptHouseholdsComplaint().get(0)
				.getCptHouseholdMembersComplaints().stream()
				.filter(obj -> obj.getIsPresentedComplaint() != null && obj.getIsPresentedComplaint()).findAny();
		if (optionalSelectedMember.isPresent()) {
			selectedMemberFistName = optionalSelectedMember.get().getFirstName();
			selectedMemberLastName = optionalSelectedMember.get().getLastName();
		}

		CptComplaintsStatus cptComplaintStatus = tmp.getCptComplaintsStatuses().stream()
				.filter(obj -> obj.getClosedAt() == null).findAny().get();

		String complaintNumber = !utilities.isNullOrEmpty(tmp.getComplaintsNumber()) ? tmp.getComplaintsNumber() : "";
		String householdCode = !utilities.isNullOrEmpty(tmp.getCptHouseholdsComplaint().get(0).getHouseholdCode())
				? tmp.getCptHouseholdsComplaint().get(0).getHouseholdCode()
				: "";
		String address = !utilities.isNullOrEmpty(tmp.getCptHouseholdsComplaint().get(0).getAddress())
				? tmp.getCptHouseholdsComplaint().get(0).getAddress()
				: "";
		String telephone = !utilities.isNullOrEmpty(tmp.getCptHouseholdsComplaint().get(0).getTelephone())
				? tmp.getCptHouseholdsComplaint().get(0).getTelephone()
				: "";
		String complaintType = !utilities.isNull(tmp.getCptComplaintType()) ? tmp.getCptComplaintType().getName() : "";

		String district = !utilities.isNull(tmp.getCptHouseholdsComplaint().get(0).getCptDistrict())
				? tmp.getCptHouseholdsComplaint().get(0).getCptDistrict().getName()
				: "";
		String ta = !utilities.isNull(tmp.getCptHouseholdsComplaint().get(0).getCptTa())
				? tmp.getCptHouseholdsComplaint().get(0).getCptTa().getName()
				: "";
		String village = !utilities.isNull(tmp.getCptHouseholdsComplaint().get(0).getCptVillage())
				? tmp.getCptHouseholdsComplaint().get(0).getCptVillage().getName()
				: "";
		String zone = !utilities.isNull(tmp.getCptHouseholdsComplaint().get(0).getCptZone())
				? tmp.getCptHouseholdsComplaint().get(0).getCptZone().getName()
				: "";

		String transferInstitution = !utilities.isNull(tmp.getCptTransferInstitution())
				? tmp.getCptTransferInstitution().getName()
				: "";
		String agency = !utilities.isNullOrEmpty(tmp.getAgencyName()) ? tmp.getAgencyName() : "";
		String complaintStatus = cptComplaintStatus.getStatus().name();
		String createdBy = !utilities.isNullOrEmpty(tmp.getCreatedBy()) ? tmp.getCreatedBy() : "";
		String observation = !utilities.isNullOrEmpty(tmp.getObservation()) ? tmp.getObservation() : "";
		String officerName = !utilities.isNullOrEmpty(tmp.getOfficerName()) ? tmp.getOfficerName() : "";
		String complaintDate = !utilities.isNull(tmp.getCreatedAt())
				? utilities.formatDate(tmp.getCreatedAt(), "dd/MMM/yyyy")
				: "";

		String action = "";
		String result = "";
		String dateLastAction = "";
		String userRegisterAction = "";
		String resultDetails = "";

		Optional<CptComplaintsActionRegistry> optCptComplaintActionRegistry = tmp.getCptComplaintsActionRegistries().stream()
				.filter(obj -> obj.getClosedAt() == null).findAny();
		if (optCptComplaintActionRegistry.isPresent()) {
			CptComplaintsActionRegistry cptComplaintActionRegistry = optCptComplaintActionRegistry.get();
			action = !utilities.isNull(cptComplaintActionRegistry.getCptComplaintsAction())
					? cptComplaintActionRegistry.getCptComplaintsAction().getName()
					: "";
			result = !utilities.isNull(cptComplaintActionRegistry.getActionResult())
					? cptComplaintActionRegistry.getActionResult().name()
					: "";
			dateLastAction = !utilities.isNull(cptComplaintActionRegistry.getActionDate())
					? utilities.formatDate(cptComplaintActionRegistry.getActionDate(), "dd/MMM/yyyy")
					: "";
			userRegisterAction = !utilities.isNullOrEmpty(cptComplaintActionRegistry.getUsernameCreatedBy())
					? cptComplaintActionRegistry.getUsernameCreatedBy()
					: "";
			resultDetails = !utilities.isNullOrEmpty(cptComplaintActionRegistry.getDetails())
					? cptComplaintActionRegistry.getDetails()
					: "";
		}

		String[] array = new String[] { district, ta, village, zone, householdCode, complaintNumber,
				selectedMemberFistName + " " + selectedMemberLastName, address, telephone, complaintType, createdBy,
				transferInstitution, agency, observation, officerName, complaintDate, complaintStatus, action, result,
				resultDetails, dateLastAction, userRegisterAction };

		return array;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CptComplaint> searchComplaints(SearchComplaintDTO searchComplaintDTO) throws ServiceException {
		try {

			log.info("searchComplaintDTO = {}", searchComplaintDTO);

			List<Specification<CptComplaint>> searchSpecifications = new ArrayList<>();

			if (!utilities.isNullOrEmpty(searchComplaintDTO.getComplaintNumber())) {
				searchSpecifications
						.add(ComplaintsSpecs.getComplaintByComplaintNumber(searchComplaintDTO.getComplaintNumber()));
			}

			if (!utilities.isNullOrEmpty(searchComplaintDTO.getHouseholdCode())) {
				searchSpecifications
						.add(ComplaintsSpecs.getComplaintByHouseholdCode(searchComplaintDTO.getHouseholdCode()));
			}

			if (utilities.isObjectIdentifiable(searchComplaintDTO.getProgram())) {
				searchSpecifications.add(ComplaintsSpecs
						.getComplaintByProgram(CptProgram.builder().id(searchComplaintDTO.getProgram().getId()).build()));
			}

			if (utilities.isObjectIdentifiable(searchComplaintDTO.getDistrict())) {
				searchSpecifications.add(ComplaintsSpecs.getComplaintsByDistrict(
						CptDistrict.builder().id(searchComplaintDTO.getDistrict().getId()).build()));
			}

			if (utilities.isObjectIdentifiable(searchComplaintDTO.getTa())) {
				searchSpecifications.add(
						ComplaintsSpecs.getComplaintByTA(CptTa.builder().id(searchComplaintDTO.getTa().getId()).build()));
			}

			if (utilities.isObjectIdentifiable(searchComplaintDTO.getVillage())) {
				searchSpecifications.add(ComplaintsSpecs
						.getComplaintByVillage(CptVillage.builder().id(searchComplaintDTO.getVillage().getId()).build()));
			}

			if (utilities.isObjectIdentifiable(searchComplaintDTO.getZone())) {
				searchSpecifications.add(ComplaintsSpecs
						.getComplaintsByZone(CptZone.builder().id(searchComplaintDTO.getZone().getId()).build()));
			}

			if (!utilities.isNullOrEmpty(searchComplaintDTO.getFirstNameMemberHowPresentComplaint())
					|| !utilities.isNullOrEmpty(searchComplaintDTO.getLastNameMemberHowPresentComplaint())) {
				searchSpecifications.add(ComplaintsSpecs.getComplaintByFirstNameMemberHowPresentComplaint(
						searchComplaintDTO.getFirstNameMemberHowPresentComplaint(),
						searchComplaintDTO.getLastNameMemberHowPresentComplaint()));
			}

			if (utilities.isObjectIdentifiable(searchComplaintDTO.getTransferInstitution())) {
				searchSpecifications.add(ComplaintsSpecs.getComplaintByTransferInstitution(
						CptTransferInstitution.builder().id(searchComplaintDTO.getTransferInstitution().getId()).build()));
			}

			if (!utilities.isNull(searchComplaintDTO.getStatus())) {
				searchSpecifications.add(ComplaintsSpecs
						.getComplaintByStatus(CptComplaintsStatusEnum.valueOf(searchComplaintDTO.getStatus().name())));
			}

			if (!utilities.isNullOrEmpty(searchComplaintDTO.getUserNameCreatedBy())) {
				searchSpecifications
						.add(ComplaintsSpecs.getComplaintByCreatedByUsername(searchComplaintDTO.getUserNameCreatedBy()));
			}

			List<CptComplaint> complaintList = new ArrayList<>();
			Pageable sortedByPriceDesc = PageRequest.of(0, 100, Sort.by("id").descending());
			log.info("searchSpecifications.isEmpty() = {} ", searchSpecifications.isEmpty());
			if (searchSpecifications.isEmpty()) {
				complaintList = complaintRepository.findAll(sortedByPriceDesc).toList();
			} else {
				complaintList = searhComplaints(searchSpecifications, sortedByPriceDesc);
			}

			complaintList.stream().forEach(obj -> prepareteCptComplaintToReturn(obj));
			return complaintList;

		} catch (DataAccessException e) {
			log.error("searchComplaints = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	private List<CptComplaint> searhComplaints(List<Specification<CptComplaint>> searchSpecifications,
			Pageable sortedByPriceDesc) {
		List<CptComplaint> complaintList;
		Specification<CptComplaint> specificaitionComplaintTmp = null;
		int cont = 0;
		for (Specification<CptComplaint> specification : searchSpecifications) {
			if (cont == 0) {
				specificaitionComplaintTmp = Specification.where(specification);
				cont = 1;
			} else {
				specificaitionComplaintTmp = specificaitionComplaintTmp.and(specification);
			}
		}
		complaintList = complaintRepository.findAll(specificaitionComplaintTmp, sortedByPriceDesc).toList();
		return complaintList;
	}

	@Override
	@Transactional
	public CptComplaint registerNewComplaintAction(
			RegisterNewComplaintActionDTO registerNewComplaintActionDTO) throws ServiceException {
		try {

			Date currentDate = Calendar.getInstance().getTime();
			CptComplaint newComplaint = complaintRepository.findById(registerNewComplaintActionDTO.getComplaintId())
					.orElseThrow(() -> new ServiceException("Complaint not found"));
			CptComplaintsAction cptComplaintAction = catalogService
					.findCptComplaintsActionById(registerNewComplaintActionDTO.getCptComplaintsAction().getId());
			CptComplaintsActionResultEnum complaintActionResult = CptComplaintsActionResultEnum
					.valueOf(registerNewComplaintActionDTO.getComplaintActionResult().name());

			newComplaint.getCptComplaintsActionRegistries().stream().forEach(obj -> {
				if (obj.getClosedAt() == null) {
					obj.setClosedAt(currentDate);
				}
			});

			log.info("ComplaintAction = {}", cptComplaintAction);

			newComplaint.getCptComplaintsActionRegistries()
					.add(CptComplaintsActionRegistry.builder().cptComplaintsAction(cptComplaintAction)
							.complaintsDetails(registerNewComplaintActionDTO.getComplaintDetails())
							.actionResult(complaintActionResult)
							.details(registerNewComplaintActionDTO.getComplaintResultDetails())
							.actionDate(registerNewComplaintActionDTO.getComplaintActionDate())
							.usernameCreatedBy(registerNewComplaintActionDTO.getCreatedByUsername()).createdAt(currentDate)
							.build());

			newComplaint.getCptComplaintsStatuses().stream().forEach(cptComplaintsStatusesTmp -> {
				if (cptComplaintsStatusesTmp.getClosedAt() == null) {
					cptComplaintsStatusesTmp.setClosedAt(currentDate);
				}
			});

			CptComplaintsStatusEnum newStatus = complaintActionResult.equals(CptComplaintsActionResultEnum.REFERRALS)?CptComplaintsStatusEnum.REFERRALS
					:complaintActionResult.equals(CptComplaintsActionResultEnum.CLOSE)?CptComplaintsStatusEnum.CLOSED:CptComplaintsStatusEnum.OPEN;
			
			newComplaint.getCptComplaintsStatuses()
			.add(CptComplaintsStatus.builder().createdAt(currentDate)
					.status(newStatus)
					.createdAt(currentDate)
					.usernameCreatedBy(registerNewComplaintActionDTO.getCreatedByUsername()).build());
			
			

			log.info("newCptComplaint= {} ", newComplaint);
			newComplaint = complaintRepository.save(newComplaint);
			return newComplaint;
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error("registerNewComplaintAction = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}
}
