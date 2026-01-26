 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.CourseenrollmentEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.CourseenrollmentVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.CourseenrollmentView;

  /**
   * 课程报名记录
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CourseenrollmentService extends IService<CourseenrollmentEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<CourseenrollmentVO> selectListVO(QueryWrapper<CourseenrollmentEntity> wrapper);
      
      CourseenrollmentVO selectVO(@Param("ew") QueryWrapper<CourseenrollmentEntity> wrapper);
      
      List<CourseenrollmentView> selectListView(QueryWrapper<CourseenrollmentEntity> wrapper);
      
      CourseenrollmentView selectView(@Param("ew") QueryWrapper<CourseenrollmentEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<CourseenrollmentEntity> wrapper);


  }
