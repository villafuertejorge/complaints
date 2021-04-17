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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.soproen.complaintsmodule.app.enums.CptComplaintsStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cpt_complaints_statuses")
public class CptComplaintsStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private CptComplaintsStatusEnum status;
	
	@Column(name = "closed_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date closedAt;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "created_by")
	private String usernameCreatedBy;
	
	@Column(name = "error_description")
	private String errorDescription;

}
