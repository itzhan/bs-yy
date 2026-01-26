 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.CancelcourseenrollmentEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.CancelcourseenrollmentVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.CancelcourseenrollmentView;

  /**
   * 取消课程报名记录
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CancelcourseenrollmentService extends IService<CancelcourseenrollmentEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<CancelcourseenrollmentVO> selectListVO(QueryWrapper<CancelcourseenrollmentEntity> wrapper);
      
      CancelcourseenrollmentVO selectVO(@Param("ew") QueryWrapper<CancelcourseenrollmentEntity> wrapper);
      
      List<CancelcourseenrollmentView> selectListView(QueryWrapper<CancelcourseenrollmentEntity> wrapper);
      
      CancelcourseenrollmentView selectView(@Param("ew") QueryWrapper<CancelcourseenrollmentEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<CancelcourseenrollmentEntity> wrapper);


  }
