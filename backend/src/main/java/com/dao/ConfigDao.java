 package com.dao;

  import com.entity.ConfigEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.ConfigVO;
  import com.entity.view.ConfigView;

  /**
   * 轮播图
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ConfigDao extends BaseMapper<ConfigEntity> {
      
      List<ConfigVO> selectListVO(@Param("ew") QueryWrapper<ConfigEntity> wrapper);
      
      ConfigVO selectVO(@Param("ew") QueryWrapper<ConfigEntity> wrapper);
      
      List<ConfigView> selectListView(@Param("ew") QueryWrapper<ConfigEntity> wrapper);

      List<ConfigView> selectListView(Page page, @Param("ew") QueryWrapper<ConfigEntity> wrapper);

      ConfigView selectView(@Param("ew") QueryWrapper<ConfigEntity> wrapper);


  }
