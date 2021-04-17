package com.soproen.complaintsmodule.app.service.catalog;

import java.util.List;

import com.soproen.complaintsdto.dto.catalog.CptSaveProgramWithComplaintsTypesDTO;
import com.soproen.complaintsmodule.app.exceptions.ServiceException;
import com.soproen.complaintsmodule.app.model.catalog.CptComplaintsAction;
import com.soproen.complaintsmodule.app.model.catalog.CptComplaintsType;
import com.soproen.complaintsmodule.app.model.catalog.CptDistrict;
import com.soproen.complaintsmodule.app.model.catalog.CptProgram;
import com.soproen.complaintsmodule.app.model.catalog.CptTa;
import com.soproen.complaintsmodule.app.model.catalog.CptTransferInstitution;
import com.soproen.complaintsmodule.app.model.catalog.CptVillage;
import com.soproen.complaintsmodule.app.model.catalog.CptZone;

public interface CatalogService {

	List<CptProgram> retrieveAllPrograms() throws ServiceException;

	void saveProgramWithComplaintsTypes(CptSaveProgramWithComplaintsTypesDTO cptSaveProgramWithComplaintsTypesDTO)
			throws ServiceException;

	List<CptComplaintsType> retrieveAllComplaintsTypes() throws ServiceException;

	List<CptDistrict> retrieveAllDistricts() throws ServiceException;
	
	List<CptTa> retrieveAllTaByDistrict(CptDistrict cptDistrict) throws ServiceException;
	
	List<CptVillage> retrieveAllVillageByTa(CptTa cptTa) throws ServiceException;
	
	List<CptZone> retrieveAllZoneByVillage(CptVillage cptVillage) throws ServiceException;
	
	CptDistrict findCptDistrictById(Long id) throws ServiceException;
	
	CptTa findCptTaById(Long id) throws ServiceException;
	
	CptVillage findCptVillageById(Long id) throws ServiceException;
	
	CptZone findCptZoneById(Long id) throws ServiceException;
	
	CptProgram findCptProgramById(Long id) throws ServiceException;
	
	CptComplaintsType findCptComplaintsTypeById(Long id) throws ServiceException;

	List<CptTransferInstitution> retrieveAllTransferInstitutions() throws ServiceException;

	CptTransferInstitution findCptTransferInstitutionById(Long id) throws ServiceException;

	List<CptComplaintsAction> retrieveAllComplaintsActions() throws ServiceException;

	CptComplaintsAction findCptComplaintsActionById(Long id) throws ServiceException;

}
