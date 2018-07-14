package com.zsf.service;

import com.zsf.domain.ProjectDto;
import com.zsf.domain.SvgInfo;

import java.util.List;

public interface ISvgService {

    int insert(SvgInfo svgInfo);

    int update(SvgInfo svgInfo);

    int delete(ProjectDto projectDto);

    SvgInfo select(int id);

    SvgInfo selectBySvgId(int svgIndex);

    List<SvgInfo> selectAllSvgs();
}
