package com.dao;

import com.entity.TrainingrecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.entity.vo.TrainingrecordVO;
import com.entity.view.TrainingrecordView;

/**
 * 训练记录
 * 
 * @author ds
 * @email 1312461553@qq.com
 */
public interface TrainingrecordDao extends BaseMapper<TrainingrecordEntity> {

    List<TrainingrecordVO> selectListVO(@Param("ew") QueryWrapper<TrainingrecordEntity> wrapper);

    TrainingrecordVO selectVO(@Param("ew") QueryWrapper<TrainingrecordEntity> wrapper);

    List<TrainingrecordView> selectListView(@Param("ew") QueryWrapper<TrainingrecordEntity> wrapper);

    List<TrainingrecordView> selectListView(Page page, @Param("ew") QueryWrapper<TrainingrecordEntity> wrapper);

    TrainingrecordView selectView(@Param("ew") QueryWrapper<TrainingrecordEntity> wrapper);
}
