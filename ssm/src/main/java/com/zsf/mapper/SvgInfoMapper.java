package com.zsf.mapper;

import com.zsf.domain.SvgInfo;

import java.util.List;

public interface SvgInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SvgInfo record);

    int insertSelective(SvgInfo record);

    SvgInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SvgInfo record);

    int updateByPrimaryKey(SvgInfo record);

    List<SvgInfo> selectAllSvg();

    SvgInfo selectBySvgId(int svgId);
}