 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.RefundcardrenewalDao;
  import com.entity.RefundcardrenewalEntity;
  import com.service.RefundcardrenewalService;
  import com.entity.vo.RefundcardrenewalVO;
  import com.entity.view.RefundcardrenewalView;

  @Service("refundcardrenewalService")
  public class RefundcardrenewalServiceImpl extends ServiceImpl<RefundcardrenewalDao, RefundcardrenewalEntity> implements RefundcardrenewalService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<RefundcardrenewalEntity> page = this.page(
                  new Query<RefundcardrenewalEntity>(params).getPage(),
                  new QueryWrapper<RefundcardrenewalEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<RefundcardrenewalEntity> wrapper) {
          Page<RefundcardrenewalView> page = new Query<RefundcardrenewalView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<RefundcardrenewalVO> selectListVO(QueryWrapper<RefundcardrenewalEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public RefundcardrenewalVO selectVO(QueryWrapper<RefundcardrenewalEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<RefundcardrenewalView> selectListView(QueryWrapper<RefundcardrenewalEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public RefundcardrenewalView selectView(QueryWrapper<RefundcardrenewalEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
