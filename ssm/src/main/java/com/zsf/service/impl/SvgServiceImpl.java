package com.zsf.service.impl;

import com.zsf.domain.ProjectDto;
import com.zsf.domain.SvgInfo;
import com.zsf.mapper.SvgInfoMapper;
import com.zsf.service.ISvgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SvgServiceImpl implements ISvgService {

    @Autowired
    private SvgInfoMapper svgInfoMapper;

    @Override
    public int insert(SvgInfo svgInfo) {
        return svgInfoMapper.insert(svgInfo);
    }

    @Override
    public int update(SvgInfo svgInfo) {
        return svgInfoMapper.updateByPrimaryKey(svgInfo);
    }

    @Override
    public int delete(ProjectDto projectDto) {
        return svgInfoMapper.deleteByPrimaryKey(projectDto.getSvgIndex());
    }

    @Override
    public SvgInfo select(int id) {
        return svgInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public SvgInfo selectBySvgId(int svgId) {
        return svgInfoMapper.selectBySvgId(svgId);
    }

    @Override
    public List<SvgInfo> selectAllSvgs() {
        return svgInfoMapper.selectAllSvg();
    }
}
