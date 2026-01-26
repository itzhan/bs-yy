 package com.dao;

  import com.entity.ChatmessageEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.ChatmessageVO;
  import com.entity.view.ChatmessageView;

  /**
   * 消息表
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ChatmessageDao extends BaseMapper<ChatmessageEntity> {
      
      List<ChatmessageVO> selectListVO(@Param("ew") QueryWrapper<ChatmessageEntity> wrapper);
      
      ChatmessageVO selectVO(@Param("ew") QueryWrapper<ChatmessageEntity> wrapper);
      
      List<ChatmessageView> selectListView(@Param("ew") QueryWrapper<ChatmessageEntity> wrapper);

      List<ChatmessageView> selectListView(Page page, @Param("ew") QueryWrapper<ChatmessageEntity> wrapper);

      ChatmessageView selectView(@Param("ew") QueryWrapper<ChatmessageEntity> wrapper);


  }
