 package com.dao;

  import com.entity.CoachEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.CoachVO;
  import com.entity.view.CoachView;

  /**
   * 教练
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CoachDao extends BaseMapper<CoachEntity> {
      
      List<CoachVO> selectListVO(@Param("ew") QueryWrapper<CoachEntity> wrapper);
      
      CoachVO selectVO(@Param("ew") QueryWrapper<CoachEntity> wrapper);
      
      List<CoachView> selectListView(@Param("ew") QueryWrapper<CoachEntity> wrapper);

      List<CoachView> selectListView(Page page, @Param("ew") QueryWrapper<CoachEntity> wrapper);

      CoachView selectView(@Param("ew") QueryWrapper<CoachEntity> wrapper);


  }
