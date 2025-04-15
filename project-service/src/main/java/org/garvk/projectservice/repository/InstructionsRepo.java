package org.garvk.projectservice.repository;

import org.garvk.projectservice.model.Instructions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructionsRepo extends JpaRepository<Instructions, Long> {

    List<Instructions> findByProjectId(long aInProjectId);

    Optional<Instructions> findTopByProjectIdOrderByCreatedAtDesc(long aInProjectId);

}
