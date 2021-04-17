package com.soproen.complaintsmodule.app.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soproen.complaintsmodule.app.model.catalog.CptDistrict;

@Repository
public interface CptDistrictRepository extends JpaRepository<CptDistrict,Long>{

}
