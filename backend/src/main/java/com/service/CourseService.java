 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.CourseEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.CourseVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.CourseView;

  /**
   * 健身课程
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CourseService extends IService<CourseEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<CourseVO> selectListVO(QueryWrapper<CourseEntity> wrapper);
      
      CourseVO selectVO(@Param("ew") QueryWrapper<CourseEntity> wrapper);
      
      List<CourseView> selectListView(QueryWrapper<CourseEntity> wrapper);
      
      CourseView selectView(@Param("ew") QueryWrapper<CourseEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<CourseEntity> wrapper);


  }
