 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.CardapplicationDao;
  import com.entity.CardapplicationEntity;
  import com.service.CardapplicationService;
  import com.entity.vo.CardapplicationVO;
  import com.entity.view.CardapplicationView;

  @Service("cardapplicationService")
  public class CardapplicationServiceImpl extends ServiceImpl<CardapplicationDao, CardapplicationEntity> implements CardapplicationService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<CardapplicationEntity> page = this.page(
                  new Query<CardapplicationEntity>(params).getPage(),
                  new QueryWrapper<CardapplicationEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CardapplicationEntity> wrapper) {
          Page<CardapplicationView> page = new Query<CardapplicationView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<CardapplicationVO> selectListVO(QueryWrapper<CardapplicationEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public CardapplicationVO selectVO(QueryWrapper<CardapplicationEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<CardapplicationView> selectListView(QueryWrapper<CardapplicationEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public CardapplicationView selectView(QueryWrapper<CardapplicationEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
