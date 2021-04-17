package com.soproen.complaintsmodule.app.repository.catalog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soproen.complaintsmodule.app.model.catalog.CptDistrict;
import com.soproen.complaintsmodule.app.model.catalog.CptTa;

@Repository
public interface CptTaRepository extends JpaRepository<CptTa,Long>{

	List<CptTa> findAllByCptDistrict(CptDistrict cptDistrict);

}
