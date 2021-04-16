package com.soproen.complaintsmodule.app.model.catalog;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.soproen.complaintsmodule.app.enums.YesNoEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cpt_programs")
public class CptProgram implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private String name;
	
	@Column(name="is_editable")
	@Enumerated(EnumType.STRING)
	private YesNoEnum isEditable;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "cpt_program_complaints_types", joinColumns = @JoinColumn(name = "program_id"), 
	inverseJoinColumns = @JoinColumn(name = "complaints_type_id"), uniqueConstraints = {	
			@UniqueConstraint(columnNames = { "program_id", "complaints_type_id" }) })
	private List<CptComplainsType> cptComplaintsTypes;
	
	

}
