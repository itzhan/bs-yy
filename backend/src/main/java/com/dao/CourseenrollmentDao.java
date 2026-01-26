 package com.dao;

  import com.entity.CourseenrollmentEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.CourseenrollmentVO;
  import com.entity.view.CourseenrollmentView;

  /**
   * 课程报名记录
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CourseenrollmentDao extends BaseMapper<CourseenrollmentEntity> {
      
      List<CourseenrollmentVO> selectListVO(@Param("ew") QueryWrapper<CourseenrollmentEntity> wrapper);
      
      CourseenrollmentVO selectVO(@Param("ew") QueryWrapper<CourseenrollmentEntity> wrapper);
      
      List<CourseenrollmentView> selectListView(@Param("ew") QueryWrapper<CourseenrollmentEntity> wrapper);

      List<CourseenrollmentView> selectListView(Page page, @Param("ew") QueryWrapper<CourseenrollmentEntity> wrapper);

      CourseenrollmentView selectView(@Param("ew") QueryWrapper<CourseenrollmentEntity> wrapper);


  }
