package com.soproen.complaintsmodule.app.model.complaints;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.soproen.complaintsmodule.app.enums.CptComplaintsActionResultEnum;
import com.soproen.complaintsmodule.app.model.catalog.CptComplaintsAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cpt_complaints_actions_registry")
public class CptComplaintsActionRegistry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "complaints_action_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "cptComplaintsActionRegistry" })
	private CptComplaintsAction cptComplaintsAction;
	
	@Column(name = "details")
	private String details;
	
	@Column(name = "action_result")
	@Enumerated(EnumType.STRING)
	private CptComplaintsActionResultEnum actionResult;
	
	@Column(name = "closed_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date closedAt;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "created_by")
	private String usernameCreatedBy;
	
	@Column(name = "observation")
	private String observation;
	
	@Column(name = "complaints_details")
	private String complaintsDetails;
	
	@Column(name = "action_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date actionDate;

}
