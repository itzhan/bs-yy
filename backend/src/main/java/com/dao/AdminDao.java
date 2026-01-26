 package com.dao;

  import com.entity.AdminEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.AdminVO;
  import com.entity.view.AdminView;

  /**
   * 管理员
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface AdminDao extends BaseMapper<AdminEntity> {
      
      List<AdminVO> selectListVO(@Param("ew") QueryWrapper<AdminEntity> wrapper);
      
      AdminVO selectVO(@Param("ew") QueryWrapper<AdminEntity> wrapper);
      
      List<AdminView> selectListView(@Param("ew") QueryWrapper<AdminEntity> wrapper);

      List<AdminView> selectListView(Page page, @Param("ew") QueryWrapper<AdminEntity> wrapper);

      AdminView selectView(@Param("ew") QueryWrapper<AdminEntity> wrapper);


  }
