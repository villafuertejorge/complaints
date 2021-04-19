package com.soproen.complaintsmodule.app.service.complaints;

import java.io.InputStream;
import java.util.List;

import com.soproen.complaintsdto.dto.complaint.CptComplaintDTO;
import com.soproen.complaintsdto.dto.complaint.RegisterNewComplaintActionDTO;
import com.soproen.complaintsdto.dto.complaint.RegisterNewComplaintForHouseholdDTO;
import com.soproen.complaintsdto.dto.complaint.SearchComplaintDTO;
import com.soproen.complaintsmodule.app.exceptions.ServiceException;
import com.soproen.complaintsmodule.app.model.complaints.CptComplaint;

public interface ComplaintsService {

	CptComplaint registerNewComplaintsForHousehold(RegisterNewComplaintForHouseholdDTO registerNewComplaintForHouseholdDTO)
			throws ServiceException;

	CptComplaint findCptComplaintById(Long idCptComplaint) throws ServiceException; 

	CptComplaint updateComplaint(CptComplaintDTO cptComplaintDTO) throws ServiceException;

	List<CptComplaint> searchComplaints(SearchComplaintDTO searchComplaintDTO) throws ServiceException;

	CptComplaint registerNewComplaintAction(RegisterNewComplaintActionDTO registerNewComplaintForHouseholdDTO) throws ServiceException;

	InputStream generateSearchComplaintsCsvFile(SearchComplaintDTO searchComplaintDTO) throws ServiceException;

}
