package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.PaymentsettingDao;
import com.entity.PaymentsettingEntity;
import com.service.PaymentsettingService;
import com.entity.vo.PaymentsettingVO;
import com.entity.view.PaymentsettingView;

@Service("paymentsettingService")
public class PaymentsettingServiceImpl extends ServiceImpl<PaymentsettingDao, PaymentsettingEntity> implements PaymentsettingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<PaymentsettingEntity> page = this.page(
                new Query<PaymentsettingEntity>(params).getPage(),
                new QueryWrapper<PaymentsettingEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<PaymentsettingEntity> wrapper) {
        Page<PaymentsettingView> page = new Query<PaymentsettingView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<PaymentsettingVO> selectListVO(QueryWrapper<PaymentsettingEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public PaymentsettingVO selectVO(QueryWrapper<PaymentsettingEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<PaymentsettingView> selectListView(QueryWrapper<PaymentsettingEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public PaymentsettingView selectView(QueryWrapper<PaymentsettingEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
