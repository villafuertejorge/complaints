package com.soproen.complaintsmodule.app.model.complaints;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.soproen.complaintsmodule.app.model.catalog.CptDistrict;
import com.soproen.complaintsmodule.app.model.catalog.CptProgram;
import com.soproen.complaintsmodule.app.model.catalog.CptTa;
import com.soproen.complaintsmodule.app.model.catalog.CptVillage;
import com.soproen.complaintsmodule.app.model.catalog.CptZone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cpt_households_complaints")
public class CptHouseholdComplaint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="program_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptProgram cptProgram;
	
	@ManyToOne
	@JoinColumn(name="district_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptDistrict cptDistrict;
	
	@ManyToOne
	@JoinColumn(name="ta_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptTa cptTa;
	
	@ManyToOne
	@JoinColumn(name="village_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptVillage cptVillage;
	
	@ManyToOne
	@JoinColumn(name="zone_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CptZone cptZone;
	
	@Column(name="household_id")
	private Long householdId;
	
	@Column(name="household_code")
	private String householdCode;

	@Column(name="address")
	private String address;
	
	@Column(name="telephone")
	private String telephone;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties(value={ "hibernateLazyInitializer", "handler","cptHouseholdComplaint" } ,allowSetters = true)
	@JoinColumn(name = "household_complaints_id",nullable = false)
	private List<CptHouseholdMemberComplaints> cptHouseholdMembersComplaints;
}
