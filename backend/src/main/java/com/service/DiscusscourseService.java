 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.DiscusscourseEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.DiscusscourseVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.DiscusscourseView;

  /**
   * 健身课程评论表
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface DiscusscourseService extends IService<DiscusscourseEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<DiscusscourseVO> selectListVO(QueryWrapper<DiscusscourseEntity> wrapper);
      
      DiscusscourseVO selectVO(@Param("ew") QueryWrapper<DiscusscourseEntity> wrapper);
      
      List<DiscusscourseView> selectListView(QueryWrapper<DiscusscourseEntity> wrapper);
      
      DiscusscourseView selectView(@Param("ew") QueryWrapper<DiscusscourseEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<DiscusscourseEntity> wrapper);


  }
