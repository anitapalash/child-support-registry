package com.kurdev.child_support_registry.repository;

import com.kurdev.child_support_registry.domain.Debtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DebtorsRepository extends JpaRepository<Debtor, Long> {

    @Modifying
    @Query(value = "update debtors set deleted = true where id = :id", nativeQuery = true)
    void delete(@Param("id") Long id);

    @Query(value = "select d.* from debtors d join children c on c.debtor_id = d.id where c.id = :childId", nativeQuery = true)
    Optional<Debtor> findByChildId(@Param("childId") Long childId);
}
