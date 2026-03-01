package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.CoachreviewDao;
import com.entity.CoachreviewEntity;
import com.service.CoachreviewService;
import com.entity.vo.CoachreviewVO;
import com.entity.view.CoachreviewView;

@Service("coachreviewService")
public class CoachreviewServiceImpl extends ServiceImpl<CoachreviewDao, CoachreviewEntity> implements CoachreviewService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CoachreviewEntity> page = this.page(
                new Query<CoachreviewEntity>(params).getPage(),
                new QueryWrapper<CoachreviewEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CoachreviewEntity> wrapper) {
        Page<CoachreviewView> page = new Query<CoachreviewView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<CoachreviewVO> selectListVO(QueryWrapper<CoachreviewEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public CoachreviewVO selectVO(QueryWrapper<CoachreviewEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<CoachreviewView> selectListView(QueryWrapper<CoachreviewEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public CoachreviewView selectView(QueryWrapper<CoachreviewEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
