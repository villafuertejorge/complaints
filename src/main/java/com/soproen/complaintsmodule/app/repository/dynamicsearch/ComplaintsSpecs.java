package com.soproen.complaintsmodule.app.repository.dynamicsearch;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.soproen.complaintsmodule.app.enums.CptComplaintsStatusEnum;
import com.soproen.complaintsmodule.app.model.catalog.CptDistrict;
import com.soproen.complaintsmodule.app.model.catalog.CptProgram;
import com.soproen.complaintsmodule.app.model.catalog.CptTa;
import com.soproen.complaintsmodule.app.model.catalog.CptTransferInstitution;
import com.soproen.complaintsmodule.app.model.catalog.CptVillage;
import com.soproen.complaintsmodule.app.model.catalog.CptZone;
import com.soproen.complaintsmodule.app.model.complaints.CptComplaint;
import com.soproen.complaintsmodule.app.model.complaints.CptComplaint_;
import com.soproen.complaintsmodule.app.model.complaints.CptComplaintsStatus;
import com.soproen.complaintsmodule.app.model.complaints.CptComplaintsStatus_;
import com.soproen.complaintsmodule.app.model.complaints.CptHouseholdComplaint;
import com.soproen.complaintsmodule.app.model.complaints.CptHouseholdComplaint_;
import com.soproen.complaintsmodule.app.model.complaints.CptHouseholdMemberComplaints;
import com.soproen.complaintsmodule.app.model.complaints.CptHouseholdMemberComplaints_;

public class ComplaintsSpecs {

	public static Specification<CptComplaint> getComplaintByComplaintNumber(String complaintNumber) {
		return (root, query, criteriaBuilder) -> {
			Predicate equalPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(CptComplaint_.complaintsNumber)),
					"%" + complaintNumber.toLowerCase() + "%");
			return equalPredicate;
		};
	}

	public static Specification<CptComplaint> getComplaintByCreatedByUsername(String username) {
		return (root, query, criteriaBuilder) -> {
			Predicate equalPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(CptComplaint_.createdBy)),
					"%" + username.toLowerCase() + "%");
			return equalPredicate;
		};
	}

	public static Specification<CptComplaint> getComplaintByHouseholdCode(String householdCode) {
		return (root, query, criteriaBuilder) -> {
			ListJoin<CptComplaint, CptHouseholdComplaint> householdComplaintJoin = root
					.join(CptComplaint_.cptHouseholdsComplaint);
			Predicate equalPredicate = criteriaBuilder.like(
					criteriaBuilder.lower(householdComplaintJoin.get(CptHouseholdComplaint_.householdCode)),
					"%" + householdCode.toLowerCase() + "%");
			return equalPredicate;
		};
	}

	public static Specification<CptComplaint> getComplaintByProgram(CptProgram cptProgram) {
		return (root, query, criteriaBuilder) -> {
			ListJoin<CptComplaint, CptHouseholdComplaint> householdComplaintJoin = root
					.join(CptComplaint_.cptHouseholdsComplaint);
			Predicate equalPredicate = criteriaBuilder
					.equal(householdComplaintJoin.get(CptHouseholdComplaint_.cptProgram), cptProgram);
			return equalPredicate;
		};
	}

	public static Specification<CptComplaint> getComplaintByTransferInstitution(
			CptTransferInstitution cptTransferInstitution) {
		return (root, query, criteriaBuilder) -> {
			Predicate equalPredicate = criteriaBuilder.equal(root.get(CptComplaint_.cptTransferInstitution),
					cptTransferInstitution);
			return equalPredicate;
		};
	}

	public static Specification<CptComplaint> getComplaintsByDistrict(CptDistrict cptDistrict) {
		return (root, query, criteriaBuilder) -> {
			ListJoin<CptComplaint, CptHouseholdComplaint> householdComplaintJoin = root
					.join(CptComplaint_.cptHouseholdsComplaint);
			Predicate equalPredicate = criteriaBuilder
					.equal(householdComplaintJoin.get(CptHouseholdComplaint_.cptDistrict), cptDistrict);
			return equalPredicate;
		};
	}

	public static Specification<CptComplaint> getComplaintByTA(CptTa cptTa) {
		return (root, query, criteriaBuilder) -> {
			ListJoin<CptComplaint, CptHouseholdComplaint> householdComplaintJoin = root
					.join(CptComplaint_.cptHouseholdsComplaint);
			Predicate equalPredicate = criteriaBuilder.equal(householdComplaintJoin.get(CptHouseholdComplaint_.cptTa),
					cptTa);
			return equalPredicate;
		};
	}

	public static Specification<CptComplaint> getComplaintByVillage(CptVillage cptVillage) {
		return (root, query, criteriaBuilder) -> {
			ListJoin<CptComplaint, CptHouseholdComplaint> householdComplaintJoin = root
					.join(CptComplaint_.cptHouseholdsComplaint);
			Predicate equalPredicate = criteriaBuilder
					.equal(householdComplaintJoin.get(CptHouseholdComplaint_.cptVillage), cptVillage);
			return equalPredicate;
		};
	}

	public static Specification<CptComplaint> getComplaintsByZone(CptZone cptZone) {
		return (root, query, criteriaBuilder) -> {
			ListJoin<CptComplaint, CptHouseholdComplaint> householdComplaintJoin = root
					.join(CptComplaint_.cptHouseholdsComplaint);
			Predicate equalPredicate = criteriaBuilder.equal(householdComplaintJoin.get(CptHouseholdComplaint_.cptZone),
					cptZone);
			return equalPredicate;
		};
	}

	public static Specification<CptComplaint> getComplaintByFirstNameMemberHowPresentComplaint(String firstName,
			String lastName) {
		return (root, query, criteriaBuilder) -> {
			ListJoin<CptHouseholdComplaint, CptHouseholdMemberComplaints> householdComplaintJoin = root
					.join(CptComplaint_.cptHouseholdsComplaint)
					.join(CptHouseholdComplaint_.cptHouseholdMembersComplaints);
			List<Predicate> predicateList = new ArrayList<>();

			if (firstName != null && !firstName.isEmpty()) {
				Predicate equalPredicate = criteriaBuilder.like(
						criteriaBuilder.lower(householdComplaintJoin.get(CptHouseholdMemberComplaints_.firstName)),
						"%" + firstName.toLowerCase() + "%");
				predicateList.add(equalPredicate);
			}

			if (lastName != null && !lastName.isEmpty()) {
				Predicate equalPredicate = criteriaBuilder.like(
						criteriaBuilder.lower(householdComplaintJoin.get(CptHouseholdMemberComplaints_.lastName)),
						"%" + lastName.toLowerCase() + "%");
				predicateList.add(equalPredicate);
			}

			if (!predicateList.isEmpty()) {
				Predicate equalPredicate = criteriaBuilder
						.isTrue(householdComplaintJoin.get(CptHouseholdMemberComplaints_.isPresentedComplaint));
				predicateList.add(equalPredicate);
			}
			return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
		};
	}

	public static Specification<CptComplaint> getComplaintByStatus(CptComplaintsStatusEnum status) {
		return (root, query, criteriaBuilder) -> {
			ListJoin<CptComplaint, CptComplaintsStatus> householdComplaintJoin = root
					.join(CptComplaint_.cptComplaintsStatuses);
			Predicate equalPredicate = criteriaBuilder
					.isNull(householdComplaintJoin.get(CptComplaintsStatus_.closedAt));
			Predicate equalPredicate2 = criteriaBuilder.equal(householdComplaintJoin.get(CptComplaintsStatus_.status),
					status);
			return criteriaBuilder.and(equalPredicate, equalPredicate2);
		};
	}
}
