package com.dao;

import com.entity.CoachreviewEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.entity.vo.CoachreviewVO;
import com.entity.view.CoachreviewView;

/**
 * 教练评价
 * 
 * @author ds
 * @email 1312461553@qq.com
 */
public interface CoachreviewDao extends BaseMapper<CoachreviewEntity> {

    List<CoachreviewVO> selectListVO(@Param("ew") QueryWrapper<CoachreviewEntity> wrapper);

    CoachreviewVO selectVO(@Param("ew") QueryWrapper<CoachreviewEntity> wrapper);

    List<CoachreviewView> selectListView(@Param("ew") QueryWrapper<CoachreviewEntity> wrapper);

    List<CoachreviewView> selectListView(Page page, @Param("ew") QueryWrapper<CoachreviewEntity> wrapper);

    CoachreviewView selectView(@Param("ew") QueryWrapper<CoachreviewEntity> wrapper);
}
