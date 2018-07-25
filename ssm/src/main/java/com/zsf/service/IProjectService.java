package com.zsf.service;

import com.zsf.domain.GroupedElement;
import com.zsf.domain.ProjectDto;
import com.zsf.domain.ProjectInfo;
import com.zsf.domain.ResBody;

import java.util.List;

public interface IProjectService {
    ResBody addProjectToCollection(ProjectDto projectDto);

    ResBody getProjectsCollection();

    ResBody saveGroupedElement(GroupedElement group);
}
