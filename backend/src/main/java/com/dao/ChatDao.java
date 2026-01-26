 package com.dao;

  import com.entity.ChatEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.ChatVO;
  import com.entity.view.ChatView;

  /**
   * 在线客服
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ChatDao extends BaseMapper<ChatEntity> {
      
      List<ChatVO> selectListVO(@Param("ew") QueryWrapper<ChatEntity> wrapper);
      
      ChatVO selectVO(@Param("ew") QueryWrapper<ChatEntity> wrapper);
      
      List<ChatView> selectListView(@Param("ew") QueryWrapper<ChatEntity> wrapper);

      List<ChatView> selectListView(Page page, @Param("ew") QueryWrapper<ChatEntity> wrapper);

      ChatView selectView(@Param("ew") QueryWrapper<ChatEntity> wrapper);


  }
