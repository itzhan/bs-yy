 package com.dao;

  import com.entity.FriendEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.FriendVO;
  import com.entity.view.FriendView;

  /**
   * 好友表
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface FriendDao extends BaseMapper<FriendEntity> {
      
      List<FriendVO> selectListVO(@Param("ew") QueryWrapper<FriendEntity> wrapper);
      
      FriendVO selectVO(@Param("ew") QueryWrapper<FriendEntity> wrapper);
      
      List<FriendView> selectListView(@Param("ew") QueryWrapper<FriendEntity> wrapper);

      List<FriendView> selectListView(Page page, @Param("ew") QueryWrapper<FriendEntity> wrapper);

      FriendView selectView(@Param("ew") QueryWrapper<FriendEntity> wrapper);


  }
