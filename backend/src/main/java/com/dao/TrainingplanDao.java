package com.dao;

import com.entity.TrainingplanEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.entity.vo.TrainingplanVO;
import com.entity.view.TrainingplanView;

/**
 * 训练计划
 * 
 * @author ds
 * @email 1312461553@qq.com
 */
public interface TrainingplanDao extends BaseMapper<TrainingplanEntity> {

    List<TrainingplanVO> selectListVO(@Param("ew") QueryWrapper<TrainingplanEntity> wrapper);

    TrainingplanVO selectVO(@Param("ew") QueryWrapper<TrainingplanEntity> wrapper);

    List<TrainingplanView> selectListView(@Param("ew") QueryWrapper<TrainingplanEntity> wrapper);

    List<TrainingplanView> selectListView(Page page, @Param("ew") QueryWrapper<TrainingplanEntity> wrapper);

    TrainingplanView selectView(@Param("ew") QueryWrapper<TrainingplanEntity> wrapper);
}
