package com.glamreserve.glamreserve.entities.company;

import com.glamreserve.glamreserve.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    @Query(value = "SELECT unavailable_date FROM unavailable_dates(CURRENT_DATE, CAST((CURRENT_DATE + INTERVAL '6 month') AS date), CAST(?1 AS INTEGER))", nativeQuery = true)
    List<Date> findUnavailableDatesByCompanyId(Long companyId);

    @Query(value = "SELECT slot_start FROM available_times(CAST(?2 AS date), CAST(?1 AS INTEGER))", nativeQuery = true)
    List<Time> findAvailableSlotsByCompanyId(Long companyId, Date date);

    @Query(value = "SELECT * FROM companies WHERE owner = ?1", nativeQuery = true)
    List<Company> findCompaniesByUser(Integer userId);

    Company getCompanyById(Long id);

}
