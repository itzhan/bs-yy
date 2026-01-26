 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.CoursetypeEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.CoursetypeVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.CoursetypeView;

  /**
   * 课程分类
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CoursetypeService extends IService<CoursetypeEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<CoursetypeVO> selectListVO(QueryWrapper<CoursetypeEntity> wrapper);
      
      CoursetypeVO selectVO(@Param("ew") QueryWrapper<CoursetypeEntity> wrapper);
      
      List<CoursetypeView> selectListView(QueryWrapper<CoursetypeEntity> wrapper);
      
      CoursetypeView selectView(@Param("ew") QueryWrapper<CoursetypeEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<CoursetypeEntity> wrapper);


  }
