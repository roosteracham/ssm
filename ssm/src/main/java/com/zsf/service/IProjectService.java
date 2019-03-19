package com.zsf.service;

import com.zsf.domain.GroupedElement;
import com.zsf.domain.ProjectDto;
import com.zsf.domain.ResBody;

import javax.servlet.http.HttpServletRequest;

public interface IProjectService {
    ResBody addProjectToCollection(ProjectDto projectDto);

    ResBody getProjectsCollection();

    ResBody saveGroupedElement(GroupedElement group);

    ResBody deleteSvg(ProjectDto projectDto);

    ResBody configHtml(Integer nep, HttpServletRequest request);

    ResBody runHtml(HttpServletRequest request);
}
