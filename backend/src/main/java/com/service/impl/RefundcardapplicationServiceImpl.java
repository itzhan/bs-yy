 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.RefundcardapplicationDao;
  import com.entity.RefundcardapplicationEntity;
  import com.service.RefundcardapplicationService;
  import com.entity.vo.RefundcardapplicationVO;
  import com.entity.view.RefundcardapplicationView;

  @Service("refundcardapplicationService")
  public class RefundcardapplicationServiceImpl extends ServiceImpl<RefundcardapplicationDao, RefundcardapplicationEntity> implements RefundcardapplicationService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<RefundcardapplicationEntity> page = this.page(
                  new Query<RefundcardapplicationEntity>(params).getPage(),
                  new QueryWrapper<RefundcardapplicationEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<RefundcardapplicationEntity> wrapper) {
          Page<RefundcardapplicationView> page = new Query<RefundcardapplicationView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<RefundcardapplicationVO> selectListVO(QueryWrapper<RefundcardapplicationEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public RefundcardapplicationVO selectVO(QueryWrapper<RefundcardapplicationEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<RefundcardapplicationView> selectListView(QueryWrapper<RefundcardapplicationEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public RefundcardapplicationView selectView(QueryWrapper<RefundcardapplicationEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
