package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.PaymentsettingEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.PaymentsettingVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.PaymentsettingView;

/**
 * 支付设置
 * @author ds
 */
public interface PaymentsettingService extends IService<PaymentsettingEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PaymentsettingVO> selectListVO(QueryWrapper<PaymentsettingEntity> wrapper);

    PaymentsettingVO selectVO(@Param("ew") QueryWrapper<PaymentsettingEntity> wrapper);

    List<PaymentsettingView> selectListView(QueryWrapper<PaymentsettingEntity> wrapper);

    PaymentsettingView selectView(@Param("ew") QueryWrapper<PaymentsettingEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<PaymentsettingEntity> wrapper);
}
