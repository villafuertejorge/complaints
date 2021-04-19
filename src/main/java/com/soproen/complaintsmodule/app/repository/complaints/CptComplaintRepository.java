package com.soproen.complaintsmodule.app.repository.complaints;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.soproen.complaintsmodule.app.model.complaints.CptComplaint;

@Repository
public interface CptComplaintRepository
		extends JpaRepository<CptComplaint, Long>, JpaSpecificationExecutor<CptComplaint> {

	@Modifying
	@Query("update CptComplaint cpt SET cpt.complaintsNumber = :complaintsNumber where cpt.id = :id")
	int updateComplaintNumber(@Param("complaintsNumber") String complaintsNumber, @Param("id") Long id);
}
