package com.soproen.complaintsmodule.app.service.catalog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.soproen.complaintsmodule.app.repository.catalog.CptComplaintsActionRepositoty;
import com.soproen.complaintsmodule.app.repository.catalog.CptComplaintsTypeRepository;
import com.soproen.complaintsmodule.app.repository.catalog.CptDistrictRepository;
import com.soproen.complaintsmodule.app.repository.catalog.CptProgramRepository;
import com.soproen.complaintsmodule.app.repository.catalog.CptTaRepository;
import com.soproen.complaintsmodule.app.repository.catalog.CptTransferInstitutionRepository;
import com.soproen.complaintsmodule.app.repository.catalog.CptVillageRepository;
import com.soproen.complaintsmodule.app.repository.catalog.CptZoneRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	private CptProgramRepository cptProgramRepository;
	@Autowired
	private CptComplaintsTypeRepository cptComplaintsTypeRepository;
	@Autowired
	private CptDistrictRepository cptDistrictRepository;
	@Autowired
	private CptTaRepository cptTaRepository;
	@Autowired
	private CptVillageRepository cptVillageRepository;
	@Autowired
	private CptZoneRepository cptZoneRepository;
	@Autowired
	private CptTransferInstitutionRepository cptTransferInstitutionRepository;
	@Autowired
	private CptComplaintsActionRepositoty cptComplaintsActionRepositoty;

	@Override
	@Transactional(readOnly = true)
	public List<CptProgram> retrieveAllPrograms() throws ServiceException {
		try {
			return cptProgramRepository.findAll();
		} catch (DataAccessException e) {
			log.error("retrieveAllPrograms = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CptTransferInstitution> retrieveAllTransferInstitutions() throws ServiceException {
		try {
			return cptTransferInstitutionRepository.findAll();
		} catch (DataAccessException e) {
			log.error("retrieveAllTransferInstitutions = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<CptComplaintsType> retrieveAllComplaintsTypes() throws ServiceException {
		try {
			return cptComplaintsTypeRepository.findAll();
		} catch (DataAccessException e) {
			log.error("retrieveAllComplaintsTypes = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void saveProgramWithComplaintsTypes(CptSaveProgramWithComplaintsTypesDTO cptSaveProgramWithComplaintsTypesDTO)
			throws ServiceException {
		try {
			CptProgram cptProgram = cptProgramRepository.findById(cptSaveProgramWithComplaintsTypesDTO.getCptProgram().getId())
					.orElseThrow(() -> new ServiceException("Program not found"));
			System.out.println(cptProgram);
			cptProgram.getCptComplaintsTypes().clear();
			cptSaveProgramWithComplaintsTypesDTO.getCptComplaintsTypes().stream().forEach(obj -> {
				cptProgram.getCptComplaintsTypes().add(cptComplaintsTypeRepository.findById(obj.getId()).get());
			});
			cptProgramRepository.save(cptProgram);
		} catch (DataAccessException e) {
			log.error("saveProgramWithComplaintsTypes = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<CptDistrict> retrieveAllDistricts() throws ServiceException {
		try {
			return cptDistrictRepository.findAll();
		} catch (DataAccessException e) {
			log.error("retrieveAllDistricts = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<CptTa> retrieveAllTaByDistrict(CptDistrict cptDistrict) throws ServiceException {
		try {
			return cptTaRepository.findAllByCptDistrict(cptDistrict);
		} catch (DataAccessException e) {
			log.error("retrieveAllTaByDistrict = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<CptVillage> retrieveAllVillageByTa(CptTa cptTa) throws ServiceException {
		try {
			return cptVillageRepository.findAllByCptTa(cptTa);
		} catch (DataAccessException e) {
			log.error("retrieveAllVillageByTa = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<CptZone> retrieveAllZoneByVillage(CptVillage cptVillage) throws ServiceException {
		try {
			return cptZoneRepository.findAllByCptVillage(cptVillage);
		} catch (DataAccessException e) {
			log.error("retrieveAllZoneByVillage = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CptDistrict findCptDistrictById(Long id) throws ServiceException {
		try {
			return cptDistrictRepository.findById(id).get();
		} catch (DataAccessException e) {
			log.error("findCptDistrictById = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CptTa findCptTaById(Long id) throws ServiceException {
		try {
			return cptTaRepository.findById(id).get();
		} catch (DataAccessException e) {
			log.error("findCptTaById = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CptVillage findCptVillageById(Long id) throws ServiceException {
		try {
			return cptVillageRepository.findById(id).get();
		} catch (DataAccessException e) {
			log.error("findCptVillageById = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CptZone findCptZoneById(Long id) throws ServiceException {
		try {
			return cptZoneRepository.findById(id).get();
		} catch (DataAccessException e) {
			log.error("findCptZoneById = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CptProgram findCptProgramById(Long id) throws ServiceException {
		try {
			return cptProgramRepository.findById(id).get();
		} catch (DataAccessException e) {
			log.error("findCptProgramById = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CptComplaintsType findCptComplaintsTypeById(Long id) throws ServiceException {
		try {
			return cptComplaintsTypeRepository.findById(id).get();
		} catch (DataAccessException e) {
			log.error("findCptComplaintsTypeById = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public CptTransferInstitution findCptTransferInstitutionById(Long id) throws ServiceException {
		try {
			return cptTransferInstitutionRepository.findById(id).get();
		} catch (DataAccessException e) {
			log.error("findCptTransferInstitutionById = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<CptComplaintsAction> retrieveAllComplaintsActions() throws ServiceException {
		try {
			return cptComplaintsActionRepositoty.findAll();
		} catch (DataAccessException e) {
			log.error("retrieveAllComplaintsActions = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public CptComplaintsAction findCptComplaintsActionById(Long id) throws ServiceException {
		try {
			return cptComplaintsActionRepositoty.findById(id).get();
		} catch (DataAccessException e) {
			log.error("findCptComplaintsActionById = {} ", e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}
	
}
