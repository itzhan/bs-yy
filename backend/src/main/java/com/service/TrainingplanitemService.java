package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.TrainingplanitemEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.TrainingplanitemVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.TrainingplanitemView;

/**
 * 训练计划明细
 *
 * @author ds
 * @email 1312461553@qq.com
 */
public interface TrainingplanitemService extends IService<TrainingplanitemEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<TrainingplanitemVO> selectListVO(QueryWrapper<TrainingplanitemEntity> wrapper);

    TrainingplanitemVO selectVO(@Param("ew") QueryWrapper<TrainingplanitemEntity> wrapper);

    List<TrainingplanitemView> selectListView(QueryWrapper<TrainingplanitemEntity> wrapper);

    TrainingplanitemView selectView(@Param("ew") QueryWrapper<TrainingplanitemEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<TrainingplanitemEntity> wrapper);
}
