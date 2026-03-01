package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.TrainingrecordEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.TrainingrecordVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.TrainingrecordView;

/**
 * 训练记录
 *
 * @author ds
 * @email 1312461553@qq.com
 */
public interface TrainingrecordService extends IService<TrainingrecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<TrainingrecordVO> selectListVO(QueryWrapper<TrainingrecordEntity> wrapper);

    TrainingrecordVO selectVO(@Param("ew") QueryWrapper<TrainingrecordEntity> wrapper);

    List<TrainingrecordView> selectListView(QueryWrapper<TrainingrecordEntity> wrapper);

    TrainingrecordView selectView(@Param("ew") QueryWrapper<TrainingrecordEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<TrainingrecordEntity> wrapper);
}
