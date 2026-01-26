 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.RefundproductorderDao;
  import com.entity.RefundproductorderEntity;
  import com.service.RefundproductorderService;
  import com.entity.vo.RefundproductorderVO;
  import com.entity.view.RefundproductorderView;

  @Service("refundproductorderService")
  public class RefundproductorderServiceImpl extends ServiceImpl<RefundproductorderDao, RefundproductorderEntity> implements RefundproductorderService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<RefundproductorderEntity> page = this.page(
                  new Query<RefundproductorderEntity>(params).getPage(),
                  new QueryWrapper<RefundproductorderEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<RefundproductorderEntity> wrapper) {
          Page<RefundproductorderView> page = new Query<RefundproductorderView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<RefundproductorderVO> selectListVO(QueryWrapper<RefundproductorderEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public RefundproductorderVO selectVO(QueryWrapper<RefundproductorderEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<RefundproductorderView> selectListView(QueryWrapper<RefundproductorderEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public RefundproductorderView selectView(QueryWrapper<RefundproductorderEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
