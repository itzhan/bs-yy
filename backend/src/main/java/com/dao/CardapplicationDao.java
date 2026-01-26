 package com.dao;

  import com.entity.CardapplicationEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.CardapplicationVO;
  import com.entity.view.CardapplicationView;

  /**
   * 办卡记录
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CardapplicationDao extends BaseMapper<CardapplicationEntity> {
      
      List<CardapplicationVO> selectListVO(@Param("ew") QueryWrapper<CardapplicationEntity> wrapper);
      
      CardapplicationVO selectVO(@Param("ew") QueryWrapper<CardapplicationEntity> wrapper);
      
      List<CardapplicationView> selectListView(@Param("ew") QueryWrapper<CardapplicationEntity> wrapper);

      List<CardapplicationView> selectListView(Page page, @Param("ew") QueryWrapper<CardapplicationEntity> wrapper);

      CardapplicationView selectView(@Param("ew") QueryWrapper<CardapplicationEntity> wrapper);


  }
