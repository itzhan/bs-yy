 package com.dao;

  import com.entity.LogEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.LogVO;
  import com.entity.view.LogView;

  /**
   * 操作日志
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface LogDao extends BaseMapper<LogEntity> {
      
      List<LogVO> selectListVO(@Param("ew") QueryWrapper<LogEntity> wrapper);
      
      LogVO selectVO(@Param("ew") QueryWrapper<LogEntity> wrapper);
      
      List<LogView> selectListView(@Param("ew") QueryWrapper<LogEntity> wrapper);

      List<LogView> selectListView(Page page, @Param("ew") QueryWrapper<LogEntity> wrapper);

      LogView selectView(@Param("ew") QueryWrapper<LogEntity> wrapper);


  }
