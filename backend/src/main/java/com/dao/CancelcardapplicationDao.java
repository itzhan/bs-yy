 package com.dao;

  import com.entity.CancelcardapplicationEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.CancelcardapplicationVO;
  import com.entity.view.CancelcardapplicationView;

  /**
   * 取消办卡记录
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CancelcardapplicationDao extends BaseMapper<CancelcardapplicationEntity> {
      
      List<CancelcardapplicationVO> selectListVO(@Param("ew") QueryWrapper<CancelcardapplicationEntity> wrapper);
      
      CancelcardapplicationVO selectVO(@Param("ew") QueryWrapper<CancelcardapplicationEntity> wrapper);
      
      List<CancelcardapplicationView> selectListView(@Param("ew") QueryWrapper<CancelcardapplicationEntity> wrapper);

      List<CancelcardapplicationView> selectListView(Page page, @Param("ew") QueryWrapper<CancelcardapplicationEntity> wrapper);

      CancelcardapplicationView selectView(@Param("ew") QueryWrapper<CancelcardapplicationEntity> wrapper);


  }
