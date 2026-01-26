 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.CancelcardapplicationDao;
  import com.entity.CancelcardapplicationEntity;
  import com.service.CancelcardapplicationService;
  import com.entity.vo.CancelcardapplicationVO;
  import com.entity.view.CancelcardapplicationView;

  @Service("cancelcardapplicationService")
  public class CancelcardapplicationServiceImpl extends ServiceImpl<CancelcardapplicationDao, CancelcardapplicationEntity> implements CancelcardapplicationService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<CancelcardapplicationEntity> page = this.page(
                  new Query<CancelcardapplicationEntity>(params).getPage(),
                  new QueryWrapper<CancelcardapplicationEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CancelcardapplicationEntity> wrapper) {
          Page<CancelcardapplicationView> page = new Query<CancelcardapplicationView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<CancelcardapplicationVO> selectListVO(QueryWrapper<CancelcardapplicationEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public CancelcardapplicationVO selectVO(QueryWrapper<CancelcardapplicationEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<CancelcardapplicationView> selectListView(QueryWrapper<CancelcardapplicationEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public CancelcardapplicationView selectView(QueryWrapper<CancelcardapplicationEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
