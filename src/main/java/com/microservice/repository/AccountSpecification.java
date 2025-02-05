package com.microservice.repository;

import jakarta.persistence.criteria.Predicate;
import com.microservice.dto.AccountFilter;
import com.microservice.model.Account;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public interface AccountSpecification {

    static Specification<Account> withFilter(AccountFilter accountFilter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (accountFilter.getFirstName() != null) {
                predicates.add(byFirstName(accountFilter.getFirstName()).toPredicate(root, query, criteriaBuilder));
            }

            if (accountFilter.getLastName() != null) {
                predicates.add(byLastName(accountFilter.getLastName()).toPredicate(root, query, criteriaBuilder));
            }

            if (accountFilter.getAgeFrom() != null || accountFilter.getAgeTo() != null) {
                predicates.add(byAgeRange(accountFilter.getAgeFrom(), accountFilter.getAgeTo()).toPredicate(root, query, criteriaBuilder));
            }

            if (accountFilter.getCountry() != null) {
                predicates.add(byCountry(accountFilter.getCountry()).toPredicate(root, query, criteriaBuilder));
            }

            if (accountFilter.getCity() != null) {
                predicates.add(byCity(accountFilter.getCity()).toPredicate(root, query, criteriaBuilder));
            }

            if (accountFilter.getDeleted() != null) {
                predicates.add(byIsDeleted(accountFilter.getDeleted()).toPredicate(root, query, criteriaBuilder));
            }

            if (accountFilter.getIds() != null && !accountFilter.getIds().isEmpty()) {
                predicates.add(byIds(accountFilter.getIds()).toPredicate(root, query, criteriaBuilder));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    static Specification<Account> byFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    static Specification<Account> byLastName(String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    static Specification<Account> byAgeRange(Integer ageFrom, Integer ageTo) {
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

    static Specification<Account> byCountry(String country) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("country"), "%" + country + "%");
    }

    static Specification<Account> byCity(String city) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("city"), "%" + city + "%");
    }

    static Specification<Account> byIsDeleted(Boolean deleted) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), deleted);
    }

    static Specification<Account> byIds(String ids) {
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
