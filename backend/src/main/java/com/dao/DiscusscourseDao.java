 package com.dao;

  import com.entity.DiscusscourseEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.DiscusscourseVO;
  import com.entity.view.DiscusscourseView;

  /**
   * 健身课程评论表
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface DiscusscourseDao extends BaseMapper<DiscusscourseEntity> {
      
      List<DiscusscourseVO> selectListVO(@Param("ew") QueryWrapper<DiscusscourseEntity> wrapper);
      
      DiscusscourseVO selectVO(@Param("ew") QueryWrapper<DiscusscourseEntity> wrapper);
      
      List<DiscusscourseView> selectListView(@Param("ew") QueryWrapper<DiscusscourseEntity> wrapper);

      List<DiscusscourseView> selectListView(Page page, @Param("ew") QueryWrapper<DiscusscourseEntity> wrapper);

      DiscusscourseView selectView(@Param("ew") QueryWrapper<DiscusscourseEntity> wrapper);


  }
