package com.kurdev.child_support_registry.repository;

import com.kurdev.child_support_registry.domain.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardiansRepository extends JpaRepository<Guardian, Long> {

    @Modifying
    @Query(value = "update guardians set deleted = true where id = :id", nativeQuery = true)
    void delete(@Param("id") Long id);
}
