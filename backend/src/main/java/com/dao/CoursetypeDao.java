 package com.dao;

  import com.entity.CoursetypeEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.CoursetypeVO;
  import com.entity.view.CoursetypeView;

  /**
   * 课程分类
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CoursetypeDao extends BaseMapper<CoursetypeEntity> {
      
      List<CoursetypeVO> selectListVO(@Param("ew") QueryWrapper<CoursetypeEntity> wrapper);
      
      CoursetypeVO selectVO(@Param("ew") QueryWrapper<CoursetypeEntity> wrapper);
      
      List<CoursetypeView> selectListView(@Param("ew") QueryWrapper<CoursetypeEntity> wrapper);

      List<CoursetypeView> selectListView(Page page, @Param("ew") QueryWrapper<CoursetypeEntity> wrapper);

      CoursetypeView selectView(@Param("ew") QueryWrapper<CoursetypeEntity> wrapper);


  }
