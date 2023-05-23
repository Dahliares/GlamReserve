package com.glamreserve.glamreserve.entities.company;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

public class CompanySpecification {

    public static Specification<Company> filterByProperties(String name, String category, Double latitude, Double longitude, Double radius) {
        return (root, query, criteriaBuilder) -> {
            Predicate conjunction = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                String nameLowerCase = name.toLowerCase();
                conjunction = criteriaBuilder.and(conjunction,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + nameLowerCase + "%"));
            }

            if (category != null && !category.isEmpty()) {
                conjunction = criteriaBuilder.and(conjunction, criteriaBuilder.equal(root.get("category"), category));
            }

            if (latitude != null && longitude != null && radius != null) {
                Expression<Double> distanceExpression = criteriaBuilder.function("calc_distance", Double.class,
                        criteriaBuilder.literal(latitude), criteriaBuilder.literal(longitude),
                        root.get("latitude"), root.get("longitude")).as(Double.class);
                Expression<Double> radiusExpression = criteriaBuilder.literal(radius*1000);

                conjunction = criteriaBuilder.and(conjunction, criteriaBuilder.lessThanOrEqualTo(distanceExpression, radiusExpression));
                query.orderBy(criteriaBuilder.asc(distanceExpression));
            }

            return conjunction;
        };
    }
}