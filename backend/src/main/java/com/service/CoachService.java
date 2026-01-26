 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.CoachEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.CoachVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.CoachView;

  /**
   * 教练
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CoachService extends IService<CoachEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<CoachVO> selectListVO(QueryWrapper<CoachEntity> wrapper);
      
      CoachVO selectVO(@Param("ew") QueryWrapper<CoachEntity> wrapper);
      
      List<CoachView> selectListView(QueryWrapper<CoachEntity> wrapper);
      
      CoachView selectView(@Param("ew") QueryWrapper<CoachEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<CoachEntity> wrapper);


  }
