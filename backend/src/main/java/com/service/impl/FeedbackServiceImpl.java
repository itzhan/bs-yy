 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.FeedbackDao;
  import com.entity.FeedbackEntity;
  import com.service.FeedbackService;
  import com.entity.vo.FeedbackVO;
  import com.entity.view.FeedbackView;

  @Service("feedbackService")
  public class FeedbackServiceImpl extends ServiceImpl<FeedbackDao, FeedbackEntity> implements FeedbackService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<FeedbackEntity> page = this.page(
                  new Query<FeedbackEntity>(params).getPage(),
                  new QueryWrapper<FeedbackEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<FeedbackEntity> wrapper) {
          Page<FeedbackView> page = new Query<FeedbackView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<FeedbackVO> selectListVO(QueryWrapper<FeedbackEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public FeedbackVO selectVO(QueryWrapper<FeedbackEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<FeedbackView> selectListView(QueryWrapper<FeedbackEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public FeedbackView selectView(QueryWrapper<FeedbackEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
