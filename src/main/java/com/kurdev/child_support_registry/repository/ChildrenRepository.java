package com.kurdev.child_support_registry.repository;

import com.kurdev.child_support_registry.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildrenRepository extends JpaRepository<Child, Long> {

    List<Child> findByDebtorId(Long debtorId);

    List<Child> findByGuardianId(Long guardianId);

    @Modifying
    @Query(value = "update children set deleted = true where id = :id", nativeQuery = true)
    void delete(@Param("id") Long id);
}
