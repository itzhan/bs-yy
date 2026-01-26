 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.ChatEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.ChatVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.ChatView;

  /**
   * 在线客服
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ChatService extends IService<ChatEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<ChatVO> selectListVO(QueryWrapper<ChatEntity> wrapper);
      
      ChatVO selectVO(@Param("ew") QueryWrapper<ChatEntity> wrapper);
      
      List<ChatView> selectListView(QueryWrapper<ChatEntity> wrapper);
      
      ChatView selectView(@Param("ew") QueryWrapper<ChatEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<ChatEntity> wrapper);


  }
