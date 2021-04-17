package com.soproen.complaintsmodule.app.model.complaints;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.soproen.complaintsmodule.app.model.catalog.CptDistrict;
import com.soproen.complaintsmodule.app.model.catalog.CptProgram;
import com.soproen.complaintsmodule.app.model.catalog.CptTa;
import com.soproen.complaintsmodule.app.model.catalog.CptVillage;
import com.soproen.complaintsmodule.app.model.catalog.CptZone;

@StaticMetamodel(CptHouseholdComplaint.class)
public class CptHouseholdComplaint_ {

	public static volatile SingularAttribute<CptHouseholdComplaint, String> householdCode;
	public static volatile SingularAttribute<CptHouseholdComplaint, CptProgram> cptProgram;
	public static volatile SingularAttribute<CptHouseholdComplaint, CptDistrict> cptDistrict;
	public static volatile SingularAttribute<CptHouseholdComplaint, CptTa> cptTa;
	public static volatile SingularAttribute<CptHouseholdComplaint, CptVillage> cptVillage;
	public static volatile SingularAttribute<CptHouseholdComplaint, CptZone> cptZone;
	public static volatile ListAttribute<CptHouseholdComplaint, CptHouseholdMemberComplaints> cptHouseholdMembersComplaints;
	 
}
