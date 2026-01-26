 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.CardapplicationEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.CardapplicationVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.CardapplicationView;

  /**
   * 办卡记录
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CardapplicationService extends IService<CardapplicationEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<CardapplicationVO> selectListVO(QueryWrapper<CardapplicationEntity> wrapper);
      
      CardapplicationVO selectVO(@Param("ew") QueryWrapper<CardapplicationEntity> wrapper);
      
      List<CardapplicationView> selectListView(QueryWrapper<CardapplicationEntity> wrapper);
      
      CardapplicationView selectView(@Param("ew") QueryWrapper<CardapplicationEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<CardapplicationEntity> wrapper);


  }
