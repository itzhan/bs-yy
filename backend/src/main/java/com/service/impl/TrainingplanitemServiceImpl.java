package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.TrainingplanitemDao;
import com.entity.TrainingplanitemEntity;
import com.service.TrainingplanitemService;
import com.entity.vo.TrainingplanitemVO;
import com.entity.view.TrainingplanitemView;

@Service("trainingplanitemService")
public class TrainingplanitemServiceImpl extends ServiceImpl<TrainingplanitemDao, TrainingplanitemEntity> implements TrainingplanitemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TrainingplanitemEntity> page = this.page(
                new Query<TrainingplanitemEntity>(params).getPage(),
                new QueryWrapper<TrainingplanitemEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<TrainingplanitemEntity> wrapper) {
        Page<TrainingplanitemView> page = new Query<TrainingplanitemView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<TrainingplanitemVO> selectListVO(QueryWrapper<TrainingplanitemEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public TrainingplanitemVO selectVO(QueryWrapper<TrainingplanitemEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<TrainingplanitemView> selectListView(QueryWrapper<TrainingplanitemEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public TrainingplanitemView selectView(QueryWrapper<TrainingplanitemEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
