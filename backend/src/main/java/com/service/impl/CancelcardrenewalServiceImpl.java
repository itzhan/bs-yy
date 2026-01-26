 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.CancelcardrenewalDao;
  import com.entity.CancelcardrenewalEntity;
  import com.service.CancelcardrenewalService;
  import com.entity.vo.CancelcardrenewalVO;
  import com.entity.view.CancelcardrenewalView;

  @Service("cancelcardrenewalService")
  public class CancelcardrenewalServiceImpl extends ServiceImpl<CancelcardrenewalDao, CancelcardrenewalEntity> implements CancelcardrenewalService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<CancelcardrenewalEntity> page = this.page(
                  new Query<CancelcardrenewalEntity>(params).getPage(),
                  new QueryWrapper<CancelcardrenewalEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CancelcardrenewalEntity> wrapper) {
          Page<CancelcardrenewalView> page = new Query<CancelcardrenewalView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<CancelcardrenewalVO> selectListVO(QueryWrapper<CancelcardrenewalEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public CancelcardrenewalVO selectVO(QueryWrapper<CancelcardrenewalEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<CancelcardrenewalView> selectListView(QueryWrapper<CancelcardrenewalEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public CancelcardrenewalView selectView(QueryWrapper<CancelcardrenewalEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
