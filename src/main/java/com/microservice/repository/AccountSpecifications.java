package com.microservice.repository;

import com.microservice.model.Account;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AccountSpecifications {

    public static Specification<Account> byFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<Account> byLastName(String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<Account> byAgeRange(Integer ageFrom, Integer ageTo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ageFrom != null) {
                LocalDateTime currentDate = LocalDateTime.now();
                LocalDateTime fromDate = currentDate.minusYears(ageFrom).withDayOfYear(1); // Начало года
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("birthDate"), fromDate));
            }

            if (ageTo != null) {
                LocalDateTime currentDate = LocalDateTime.now();
                LocalDateTime toDate = currentDate.minusYears(ageTo).withDayOfYear(1).plusYears(1).minusDays(1); // Конец года
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("birthDate"), toDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Account> byCountry(String country) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("country"), "%" + country + "%");
    }

    public static Specification<Account> byCity(String city) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("city"), "%" + city + "%");
    }

    public static Specification<Account> byIsDeleted(Boolean deleted) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), deleted);
    }

    public static Specification<Account> byIds(String ids) {
        return (root, query, criteriaBuilder) -> {
            if (ids == null || ids.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String[] idArray = ids.split(",");
            UUID[] uuidArray = Arrays.stream(idArray)
                    .map(UUID::fromString)
                    .toArray(UUID[]::new);
            return root.get("id").in(uuidArray);
        };
    }
}
