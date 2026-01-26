 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.UserEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.UserVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.UserView;

  /**
   * 用户
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface UserService extends IService<UserEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<UserVO> selectListVO(QueryWrapper<UserEntity> wrapper);
      
      UserVO selectVO(@Param("ew") QueryWrapper<UserEntity> wrapper);
      
      List<UserView> selectListView(QueryWrapper<UserEntity> wrapper);
      
      UserView selectView(@Param("ew") QueryWrapper<UserEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<UserEntity> wrapper);


  }
