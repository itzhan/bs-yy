 package com.dao;

  import com.entity.CancelcourseenrollmentEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.CancelcourseenrollmentVO;
  import com.entity.view.CancelcourseenrollmentView;

  /**
   * 取消课程报名记录
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CancelcourseenrollmentDao extends BaseMapper<CancelcourseenrollmentEntity> {
      
      List<CancelcourseenrollmentVO> selectListVO(@Param("ew") QueryWrapper<CancelcourseenrollmentEntity> wrapper);
      
      CancelcourseenrollmentVO selectVO(@Param("ew") QueryWrapper<CancelcourseenrollmentEntity> wrapper);
      
      List<CancelcourseenrollmentView> selectListView(@Param("ew") QueryWrapper<CancelcourseenrollmentEntity> wrapper);

      List<CancelcourseenrollmentView> selectListView(Page page, @Param("ew") QueryWrapper<CancelcourseenrollmentEntity> wrapper);

      CancelcourseenrollmentView selectView(@Param("ew") QueryWrapper<CancelcourseenrollmentEntity> wrapper);


  }
