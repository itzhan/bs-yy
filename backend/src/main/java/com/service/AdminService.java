 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.AdminEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.AdminVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.AdminView;

  /**
   * 管理员
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface AdminService extends IService<AdminEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<AdminVO> selectListVO(QueryWrapper<AdminEntity> wrapper);
      
      AdminVO selectVO(@Param("ew") QueryWrapper<AdminEntity> wrapper);
      
      List<AdminView> selectListView(QueryWrapper<AdminEntity> wrapper);
      
      AdminView selectView(@Param("ew") QueryWrapper<AdminEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<AdminEntity> wrapper);


  }
