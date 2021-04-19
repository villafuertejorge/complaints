package com.soproen.complaintsmodule.app.model.complaints;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.soproen.complaintsmodule.app.enums.RequiredComplaintFiledsEnum;
import com.soproen.complaintsmodule.app.model.catalog.CptComplaintsType;
import com.soproen.complaintsmodule.app.model.catalog.CptDistrict;
import com.soproen.complaintsmodule.app.model.catalog.CptTa;
import com.soproen.complaintsmodule.app.model.catalog.CptTransferInstitution;
import com.soproen.complaintsmodule.app.model.catalog.CptVillage;
import com.soproen.complaintsmodule.app.model.catalog.CptZone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cpt_complaints")
public class CptComplaint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "complaints_number")
	private String complaintsNumber;

	@ManyToOne
	@JoinColumn(name = "district_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptDistrict cptDistrict;

	@ManyToOne
	@JoinColumn(name = "ta_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptTa cptTa;

	@ManyToOne
	@JoinColumn(name = "village_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptVillage cptVillage;

	@ManyToOne
	@JoinColumn(name = "zone_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptZone cptZone;

	@Column(name = "site_name")
	private String siteName;

	@ManyToOne
	@JoinColumn(name = "transfer_institution_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptTransferInstitution cptTransferInstitution;

	@Column(name = "agency_name")
	private String agencyName;

	@ManyToOne
	@JoinColumn(name = "complaints_type_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptComplaintsType cptComplaintType;

	@Column(name = "observation")
	private String observation;

	@Column(name = "officer_name")
	private String officerName;

	@Column(name = "officer_position")
	private String officerPosition;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Singular
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "cptComplaint" }, allowSetters = true)
	@JoinColumn(name = "complaints_id", nullable = false)
	private List<CptComplaintsStatus> cptComplaintsStatuses;
	
	@Singular
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "cptComplaint" }, allowSetters = true)
	@JoinColumn(name = "complaints_id", nullable = false)
	@OrderBy("id DESC")
	private List<CptComplaintsActionRegistry> cptComplaintsActionRegistries;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "cptComplaint" }, allowSetters = true)
	@JoinColumn(name = "complaints_id", nullable = false)
	private List<CptHouseholdComplaint> cptHouseholdsComplaint;
	
	@Transient
	private CptHouseholdMemberComplaints selectedMemberHowPresentComplaints;
	@Transient
	private String applicationUserName;
	@Transient
	private Map<RequiredComplaintFiledsEnum, String> mapResultComplaintFieldsValidation;

}
