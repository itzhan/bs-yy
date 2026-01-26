 package com.dao;

  import com.entity.UserEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.UserVO;
  import com.entity.view.UserView;

  /**
   * 用户
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface UserDao extends BaseMapper<UserEntity> {
      
      List<UserVO> selectListVO(@Param("ew") QueryWrapper<UserEntity> wrapper);
      
      UserVO selectVO(@Param("ew") QueryWrapper<UserEntity> wrapper);
      
      List<UserView> selectListView(@Param("ew") QueryWrapper<UserEntity> wrapper);

      List<UserView> selectListView(Page page, @Param("ew") QueryWrapper<UserEntity> wrapper);

      UserView selectView(@Param("ew") QueryWrapper<UserEntity> wrapper);


  }
