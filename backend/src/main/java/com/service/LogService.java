 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.LogEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.LogVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.LogView;

  /**
   * 操作日志
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface LogService extends IService<LogEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<LogVO> selectListVO(QueryWrapper<LogEntity> wrapper);
      
      LogVO selectVO(@Param("ew") QueryWrapper<LogEntity> wrapper);
      
      List<LogView> selectListView(QueryWrapper<LogEntity> wrapper);
      
      LogView selectView(@Param("ew") QueryWrapper<LogEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<LogEntity> wrapper);


  }
