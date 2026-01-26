 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.ChatmessageEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.ChatmessageVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.ChatmessageView;

  /**
   * 消息表
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ChatmessageService extends IService<ChatmessageEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<ChatmessageVO> selectListVO(QueryWrapper<ChatmessageEntity> wrapper);
      
      ChatmessageVO selectVO(@Param("ew") QueryWrapper<ChatmessageEntity> wrapper);
      
      List<ChatmessageView> selectListView(QueryWrapper<ChatmessageEntity> wrapper);
      
      ChatmessageView selectView(@Param("ew") QueryWrapper<ChatmessageEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<ChatmessageEntity> wrapper);


  }
