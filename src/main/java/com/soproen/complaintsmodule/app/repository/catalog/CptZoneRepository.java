package com.soproen.complaintsmodule.app.repository.catalog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soproen.complaintsmodule.app.model.catalog.CptVillage;
import com.soproen.complaintsmodule.app.model.catalog.CptZone;

@Repository
public interface CptZoneRepository  extends JpaRepository<CptZone,Long>{

	List<CptZone> findAllByCptVillage(CptVillage cptVillage);

}
