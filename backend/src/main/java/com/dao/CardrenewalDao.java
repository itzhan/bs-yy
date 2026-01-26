 package com.dao;

  import com.entity.CardrenewalEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.CardrenewalVO;
  import com.entity.view.CardrenewalView;

  /**
   * 续卡记录
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CardrenewalDao extends BaseMapper<CardrenewalEntity> {
      
      List<CardrenewalVO> selectListVO(@Param("ew") QueryWrapper<CardrenewalEntity> wrapper);
      
      CardrenewalVO selectVO(@Param("ew") QueryWrapper<CardrenewalEntity> wrapper);
      
      List<CardrenewalView> selectListView(@Param("ew") QueryWrapper<CardrenewalEntity> wrapper);

      List<CardrenewalView> selectListView(Page page, @Param("ew") QueryWrapper<CardrenewalEntity> wrapper);

      CardrenewalView selectView(@Param("ew") QueryWrapper<CardrenewalEntity> wrapper);


  }
