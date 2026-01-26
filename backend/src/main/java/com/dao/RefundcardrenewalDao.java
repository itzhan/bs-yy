 package com.dao;

  import com.entity.RefundcardrenewalEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.RefundcardrenewalVO;
  import com.entity.view.RefundcardrenewalView;

  /**
   * 续卡记录退款
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface RefundcardrenewalDao extends BaseMapper<RefundcardrenewalEntity> {
      
      List<RefundcardrenewalVO> selectListVO(@Param("ew") QueryWrapper<RefundcardrenewalEntity> wrapper);
      
      RefundcardrenewalVO selectVO(@Param("ew") QueryWrapper<RefundcardrenewalEntity> wrapper);
      
      List<RefundcardrenewalView> selectListView(@Param("ew") QueryWrapper<RefundcardrenewalEntity> wrapper);

      List<RefundcardrenewalView> selectListView(Page page, @Param("ew") QueryWrapper<RefundcardrenewalEntity> wrapper);

      RefundcardrenewalView selectView(@Param("ew") QueryWrapper<RefundcardrenewalEntity> wrapper);


  }
