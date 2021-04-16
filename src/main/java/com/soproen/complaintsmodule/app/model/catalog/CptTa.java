package com.soproen.complaintsmodule.app.model.catalog;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cpt_ta")
public class CptTa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String name;
	
	@ManyToOne
	@JoinColumn(name="district_id")
	private CptDistrict cptDistrict;

}
