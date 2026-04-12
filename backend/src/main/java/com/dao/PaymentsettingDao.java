package com.dao;

import com.entity.PaymentsettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.entity.vo.PaymentsettingVO;
import com.entity.view.PaymentsettingView;

/**
 * 支付设置
 * @author ds
 */
public interface PaymentsettingDao extends BaseMapper<PaymentsettingEntity> {

    List<PaymentsettingVO> selectListVO(@Param("ew") QueryWrapper<PaymentsettingEntity> wrapper);

    PaymentsettingVO selectVO(@Param("ew") QueryWrapper<PaymentsettingEntity> wrapper);

    List<PaymentsettingView> selectListView(@Param("ew") QueryWrapper<PaymentsettingEntity> wrapper);

    List<PaymentsettingView> selectListView(Page page, @Param("ew") QueryWrapper<PaymentsettingEntity> wrapper);

    PaymentsettingView selectView(@Param("ew") QueryWrapper<PaymentsettingEntity> wrapper);
}
