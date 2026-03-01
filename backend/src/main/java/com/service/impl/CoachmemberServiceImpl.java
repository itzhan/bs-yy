package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.CoachmemberDao;
import com.entity.CoachmemberEntity;
import com.service.CoachmemberService;
import com.entity.vo.CoachmemberVO;
import com.entity.view.CoachmemberView;

@Service("coachmemberService")
public class CoachmemberServiceImpl extends ServiceImpl<CoachmemberDao, CoachmemberEntity> implements CoachmemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CoachmemberEntity> page = this.page(
                new Query<CoachmemberEntity>(params).getPage(),
                new QueryWrapper<CoachmemberEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CoachmemberEntity> wrapper) {
        Page<CoachmemberView> page = new Query<CoachmemberView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<CoachmemberVO> selectListVO(QueryWrapper<CoachmemberEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public CoachmemberVO selectVO(QueryWrapper<CoachmemberEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<CoachmemberView> selectListView(QueryWrapper<CoachmemberEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public CoachmemberView selectView(QueryWrapper<CoachmemberEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
