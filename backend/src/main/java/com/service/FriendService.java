 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.FriendEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.FriendVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.FriendView;

  /**
   * 好友表
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface FriendService extends IService<FriendEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<FriendVO> selectListVO(QueryWrapper<FriendEntity> wrapper);
      
      FriendVO selectVO(@Param("ew") QueryWrapper<FriendEntity> wrapper);
      
      List<FriendView> selectListView(QueryWrapper<FriendEntity> wrapper);
      
      FriendView selectView(@Param("ew") QueryWrapper<FriendEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<FriendEntity> wrapper);


  }
