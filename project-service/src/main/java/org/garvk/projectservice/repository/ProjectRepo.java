package org.garvk.projectservice.repository;

import org.garvk.projectservice.model.Project;
import org.garvk.projectservice.model.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project,Long> {

    List<Project> findByCreatorId(Long aInCreatorId);

    List<Project> findByEditorId(long aInEditorId);

    List<Project> findByStatus(ProjectStatus aInProjectStatus);

    List<Project> findByCreatorIdAndStatus(long aInCreatorId, ProjectStatus aInProjectStatus);

    List<Project> findByEditorIdAndStatus(long aInEditorId, ProjectStatus aInProjectStatus);

    long countByCreatorId(long aInCreatorId);

    long countByEditorId(long aInEditorId);

}
