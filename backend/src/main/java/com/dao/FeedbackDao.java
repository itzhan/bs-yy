 package com.dao;

  import com.entity.FeedbackEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.FeedbackVO;
  import com.entity.view.FeedbackView;

  /**
   * 意见反馈
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface FeedbackDao extends BaseMapper<FeedbackEntity> {
      
      List<FeedbackVO> selectListVO(@Param("ew") QueryWrapper<FeedbackEntity> wrapper);
      
      FeedbackVO selectVO(@Param("ew") QueryWrapper<FeedbackEntity> wrapper);
      
      List<FeedbackView> selectListView(@Param("ew") QueryWrapper<FeedbackEntity> wrapper);

      List<FeedbackView> selectListView(Page page, @Param("ew") QueryWrapper<FeedbackEntity> wrapper);

      FeedbackView selectView(@Param("ew") QueryWrapper<FeedbackEntity> wrapper);


  }
