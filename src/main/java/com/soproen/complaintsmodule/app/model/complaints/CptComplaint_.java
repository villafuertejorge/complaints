package com.soproen.complaintsmodule.app.model.complaints;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.soproen.complaintsmodule.app.model.catalog.CptTransferInstitution;

@StaticMetamodel(CptComplaint.class)
public class CptComplaint_ {

	public static volatile SingularAttribute<CptComplaint, String> complaintsNumber;
	public static volatile ListAttribute<CptComplaint, CptHouseholdComplaint> cptHouseholdsComplaint;
	public static volatile SingularAttribute<CptComplaint, CptTransferInstitution> cptTransferInstitution;
	public static volatile ListAttribute<CptComplaint, CptComplaintsStatus> cptComplaintsStatuses;
	public static volatile SingularAttribute<CptComplaint, String> createdBy;
}
