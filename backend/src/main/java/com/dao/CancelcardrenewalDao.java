 package com.dao;

  import com.entity.CancelcardrenewalEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.CancelcardrenewalVO;
  import com.entity.view.CancelcardrenewalView;

  /**
   * 取消续卡记录
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CancelcardrenewalDao extends BaseMapper<CancelcardrenewalEntity> {
      
      List<CancelcardrenewalVO> selectListVO(@Param("ew") QueryWrapper<CancelcardrenewalEntity> wrapper);
      
      CancelcardrenewalVO selectVO(@Param("ew") QueryWrapper<CancelcardrenewalEntity> wrapper);
      
      List<CancelcardrenewalView> selectListView(@Param("ew") QueryWrapper<CancelcardrenewalEntity> wrapper);

      List<CancelcardrenewalView> selectListView(Page page, @Param("ew") QueryWrapper<CancelcardrenewalEntity> wrapper);

      CancelcardrenewalView selectView(@Param("ew") QueryWrapper<CancelcardrenewalEntity> wrapper);


  }
