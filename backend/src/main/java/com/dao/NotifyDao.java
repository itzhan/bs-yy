 package com.dao;

  import com.entity.NotifyEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.NotifyVO;
  import com.entity.view.NotifyView;

  /**
   * 消息通知
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface NotifyDao extends BaseMapper<NotifyEntity> {
      
      List<NotifyVO> selectListVO(@Param("ew") QueryWrapper<NotifyEntity> wrapper);
      
      NotifyVO selectVO(@Param("ew") QueryWrapper<NotifyEntity> wrapper);
      
      List<NotifyView> selectListView(@Param("ew") QueryWrapper<NotifyEntity> wrapper);

      List<NotifyView> selectListView(Page page, @Param("ew") QueryWrapper<NotifyEntity> wrapper);

      NotifyView selectView(@Param("ew") QueryWrapper<NotifyEntity> wrapper);


  }
