package com.glamreserve.glamreserve.entities.reserve;

import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    List<Reserve> findAllByUser_Id(Long userId);
    List<Reserve> findAllByCompany_Id(Long companyId);

    default List<Reserve> findByCompanyAndDate(Long companyId, LocalDate date) {
        Timestamp startOfDay = Timestamp.valueOf(date.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(date.plusDays(1).atStartOfDay());

        return findByCompanyIdAndDateBetween(companyId, startOfDay, endOfDay);
    }

    List<Reserve> findByCompanyIdAndDateBetween(Long companyId, Timestamp startOfDay, Timestamp endOfDay);

}
