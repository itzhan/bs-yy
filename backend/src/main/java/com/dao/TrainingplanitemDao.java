package com.dao;

import com.entity.TrainingplanitemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.entity.vo.TrainingplanitemVO;
import com.entity.view.TrainingplanitemView;

/**
 * 训练计划明细
 * 
 * @author ds
 * @email 1312461553@qq.com
 */
public interface TrainingplanitemDao extends BaseMapper<TrainingplanitemEntity> {

    List<TrainingplanitemVO> selectListVO(@Param("ew") QueryWrapper<TrainingplanitemEntity> wrapper);

    TrainingplanitemVO selectVO(@Param("ew") QueryWrapper<TrainingplanitemEntity> wrapper);

    List<TrainingplanitemView> selectListView(@Param("ew") QueryWrapper<TrainingplanitemEntity> wrapper);

    List<TrainingplanitemView> selectListView(Page page, @Param("ew") QueryWrapper<TrainingplanitemEntity> wrapper);

    TrainingplanitemView selectView(@Param("ew") QueryWrapper<TrainingplanitemEntity> wrapper);
}
