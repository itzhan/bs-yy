 package com.dao;

  import com.entity.RefundcardapplicationEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.RefundcardapplicationVO;
  import com.entity.view.RefundcardapplicationView;

  /**
   * 办卡记录退款
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface RefundcardapplicationDao extends BaseMapper<RefundcardapplicationEntity> {
      
      List<RefundcardapplicationVO> selectListVO(@Param("ew") QueryWrapper<RefundcardapplicationEntity> wrapper);
      
      RefundcardapplicationVO selectVO(@Param("ew") QueryWrapper<RefundcardapplicationEntity> wrapper);
      
      List<RefundcardapplicationView> selectListView(@Param("ew") QueryWrapper<RefundcardapplicationEntity> wrapper);

      List<RefundcardapplicationView> selectListView(Page page, @Param("ew") QueryWrapper<RefundcardapplicationEntity> wrapper);

      RefundcardapplicationView selectView(@Param("ew") QueryWrapper<RefundcardapplicationEntity> wrapper);


  }
