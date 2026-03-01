package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.CoachreviewEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.CoachreviewVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.CoachreviewView;

/**
 * 教练评价
 *
 * @author ds
 * @email 1312461553@qq.com
 */
public interface CoachreviewService extends IService<CoachreviewEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CoachreviewVO> selectListVO(QueryWrapper<CoachreviewEntity> wrapper);

    CoachreviewVO selectVO(@Param("ew") QueryWrapper<CoachreviewEntity> wrapper);

    List<CoachreviewView> selectListView(QueryWrapper<CoachreviewEntity> wrapper);

    CoachreviewView selectView(@Param("ew") QueryWrapper<CoachreviewEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<CoachreviewEntity> wrapper);
}
