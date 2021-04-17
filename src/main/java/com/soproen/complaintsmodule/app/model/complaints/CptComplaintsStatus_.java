package com.soproen.complaintsmodule.app.model.complaints;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.soproen.complaintsmodule.app.enums.CptComplaintsStatusEnum;

@StaticMetamodel(CptComplaintsStatus.class)
public class CptComplaintsStatus_ {

	public static volatile SingularAttribute<CptComplaintsStatus, CptComplaintsStatusEnum> status;
	public static volatile SingularAttribute<CptComplaintsStatus, Date> closedAt;
}
