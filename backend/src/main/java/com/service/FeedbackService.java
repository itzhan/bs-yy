 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.FeedbackEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.FeedbackVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.FeedbackView;

  /**
   * 意见反馈
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface FeedbackService extends IService<FeedbackEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<FeedbackVO> selectListVO(QueryWrapper<FeedbackEntity> wrapper);
      
      FeedbackVO selectVO(@Param("ew") QueryWrapper<FeedbackEntity> wrapper);
      
      List<FeedbackView> selectListView(QueryWrapper<FeedbackEntity> wrapper);
      
      FeedbackView selectView(@Param("ew") QueryWrapper<FeedbackEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<FeedbackEntity> wrapper);


  }
