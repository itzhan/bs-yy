package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.TrainingrecordDao;
import com.entity.TrainingrecordEntity;
import com.service.TrainingrecordService;
import com.entity.vo.TrainingrecordVO;
import com.entity.view.TrainingrecordView;

@Service("trainingrecordService")
public class TrainingrecordServiceImpl extends ServiceImpl<TrainingrecordDao, TrainingrecordEntity> implements TrainingrecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TrainingrecordEntity> page = this.page(
                new Query<TrainingrecordEntity>(params).getPage(),
                new QueryWrapper<TrainingrecordEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<TrainingrecordEntity> wrapper) {
        Page<TrainingrecordView> page = new Query<TrainingrecordView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<TrainingrecordVO> selectListVO(QueryWrapper<TrainingrecordEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public TrainingrecordVO selectVO(QueryWrapper<TrainingrecordEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<TrainingrecordView> selectListView(QueryWrapper<TrainingrecordEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public TrainingrecordView selectView(QueryWrapper<TrainingrecordEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
