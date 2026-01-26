 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.RefundcourseenrollmentEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.RefundcourseenrollmentVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.RefundcourseenrollmentView;

  /**
   * 课程报名记录退款
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface RefundcourseenrollmentService extends IService<RefundcourseenrollmentEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<RefundcourseenrollmentVO> selectListVO(QueryWrapper<RefundcourseenrollmentEntity> wrapper);
      
      RefundcourseenrollmentVO selectVO(@Param("ew") QueryWrapper<RefundcourseenrollmentEntity> wrapper);
      
      List<RefundcourseenrollmentView> selectListView(QueryWrapper<RefundcourseenrollmentEntity> wrapper);
      
      RefundcourseenrollmentView selectView(@Param("ew") QueryWrapper<RefundcourseenrollmentEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<RefundcourseenrollmentEntity> wrapper);


  }
