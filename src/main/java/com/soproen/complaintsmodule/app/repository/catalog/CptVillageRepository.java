package com.soproen.complaintsmodule.app.repository.catalog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soproen.complaintsmodule.app.model.catalog.CptTa;
import com.soproen.complaintsmodule.app.model.catalog.CptVillage;

@Repository
public interface CptVillageRepository extends JpaRepository<CptVillage,Long>{

	List<CptVillage> findAllByCptTa(CptTa cptTa);

}
