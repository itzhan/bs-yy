package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.TrainingplanDao;
import com.entity.TrainingplanEntity;
import com.service.TrainingplanService;
import com.entity.vo.TrainingplanVO;
import com.entity.view.TrainingplanView;

@Service("trainingplanService")
public class TrainingplanServiceImpl extends ServiceImpl<TrainingplanDao, TrainingplanEntity> implements TrainingplanService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TrainingplanEntity> page = this.page(
                new Query<TrainingplanEntity>(params).getPage(),
                new QueryWrapper<TrainingplanEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<TrainingplanEntity> wrapper) {
        Page<TrainingplanView> page = new Query<TrainingplanView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<TrainingplanVO> selectListVO(QueryWrapper<TrainingplanEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public TrainingplanVO selectVO(QueryWrapper<TrainingplanEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<TrainingplanView> selectListView(QueryWrapper<TrainingplanEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public TrainingplanView selectView(QueryWrapper<TrainingplanEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
