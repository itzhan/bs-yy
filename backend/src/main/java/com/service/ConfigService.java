 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.ConfigEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.ConfigVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.ConfigView;

  /**
   * 轮播图
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ConfigService extends IService<ConfigEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<ConfigVO> selectListVO(QueryWrapper<ConfigEntity> wrapper);
      
      ConfigVO selectVO(@Param("ew") QueryWrapper<ConfigEntity> wrapper);
      
      List<ConfigView> selectListView(QueryWrapper<ConfigEntity> wrapper);
      
      ConfigView selectView(@Param("ew") QueryWrapper<ConfigEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<ConfigEntity> wrapper);


  }
