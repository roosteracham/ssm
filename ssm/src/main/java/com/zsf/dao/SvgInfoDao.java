package com.zsf.dao;

import com.zsf.domain.ProjectDto;
import com.zsf.domain.SvgInfo;
import com.zsf.util.mapper.SvgInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SvgInfoDao {

    @Autowired
    private SvgInfoMapper svgInfoMapper;

    public int insert(SvgInfo svgInfo) {
        return svgInfoMapper.insert(svgInfo);
    }

    public int update(SvgInfo svgInfo) {
        return svgInfoMapper.updateByPrimaryKey(svgInfo);
    }

    public int delete(ProjectDto projectDto) {
        return svgInfoMapper.deleteByPrimaryKey(projectDto.getSvgIndex());
    }

    public SvgInfo select(int id) {
        return svgInfoMapper.selectByPrimaryKey(id);
    }

    public SvgInfo selectByProjectId(Integer projectId) {
        return svgInfoMapper.selectByProjectId(projectId);
    }

    public SvgInfo selectBySvgId(int svgId) {
        return svgInfoMapper.selectBySvgId(svgId);
    }

    public List<SvgInfo> selectAllSvgs() {
        return svgInfoMapper.selectAllSvg();
    }

    public void deleteSvg(SvgInfo svgInfo) {
        svgInfoMapper.deleteSvg(svgInfo);
    }

    public SvgInfo selectByPSId(SvgInfo svgInfo) {
        return svgInfoMapper.selectByPSId(svgInfo);
    }
}

