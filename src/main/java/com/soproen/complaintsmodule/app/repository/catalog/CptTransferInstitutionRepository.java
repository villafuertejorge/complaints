package com.soproen.complaintsmodule.app.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soproen.complaintsmodule.app.model.catalog.CptTransferInstitution;

@Repository
public interface CptTransferInstitutionRepository  extends JpaRepository<CptTransferInstitution,Long>{

}
