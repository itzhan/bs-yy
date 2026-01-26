 package com.dao;

  import com.entity.RefundcourseenrollmentEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.RefundcourseenrollmentVO;
  import com.entity.view.RefundcourseenrollmentView;

  /**
   * 课程报名记录退款
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface RefundcourseenrollmentDao extends BaseMapper<RefundcourseenrollmentEntity> {
      
      List<RefundcourseenrollmentVO> selectListVO(@Param("ew") QueryWrapper<RefundcourseenrollmentEntity> wrapper);
      
      RefundcourseenrollmentVO selectVO(@Param("ew") QueryWrapper<RefundcourseenrollmentEntity> wrapper);
      
      List<RefundcourseenrollmentView> selectListView(@Param("ew") QueryWrapper<RefundcourseenrollmentEntity> wrapper);

      List<RefundcourseenrollmentView> selectListView(Page page, @Param("ew") QueryWrapper<RefundcourseenrollmentEntity> wrapper);

      RefundcourseenrollmentView selectView(@Param("ew") QueryWrapper<RefundcourseenrollmentEntity> wrapper);


  }
