package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.TrainingplanEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.TrainingplanVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.TrainingplanView;

/**
 * 训练计划
 *
 * @author ds
 * @email 1312461553@qq.com
 */
public interface TrainingplanService extends IService<TrainingplanEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<TrainingplanVO> selectListVO(QueryWrapper<TrainingplanEntity> wrapper);

    TrainingplanVO selectVO(@Param("ew") QueryWrapper<TrainingplanEntity> wrapper);

    List<TrainingplanView> selectListView(QueryWrapper<TrainingplanEntity> wrapper);

    TrainingplanView selectView(@Param("ew") QueryWrapper<TrainingplanEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<TrainingplanEntity> wrapper);
}
