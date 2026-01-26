 package com.dao;

  import com.entity.CourseEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.CourseVO;
  import com.entity.view.CourseView;

  /**
   * 健身课程
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CourseDao extends BaseMapper<CourseEntity> {
      
      List<CourseVO> selectListVO(@Param("ew") QueryWrapper<CourseEntity> wrapper);
      
      CourseVO selectVO(@Param("ew") QueryWrapper<CourseEntity> wrapper);
      
      List<CourseView> selectListView(@Param("ew") QueryWrapper<CourseEntity> wrapper);

      List<CourseView> selectListView(Page page, @Param("ew") QueryWrapper<CourseEntity> wrapper);

      CourseView selectView(@Param("ew") QueryWrapper<CourseEntity> wrapper);


  }
