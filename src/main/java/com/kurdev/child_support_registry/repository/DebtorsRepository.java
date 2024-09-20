package com.kurdev.child_support_registry.repository;

import com.kurdev.child_support_registry.domain.Debtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtorsRepository extends JpaRepository<Debtor, Long> {

    @Modifying
    @Query(value = "update debtors set deleted = true where id = :id", nativeQuery = true)
    void delete(@Param("id") Long id);
}
