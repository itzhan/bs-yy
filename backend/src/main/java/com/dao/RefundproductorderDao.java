 package com.dao;

  import com.entity.RefundproductorderEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.RefundproductorderVO;
  import com.entity.view.RefundproductorderView;

  /**
   * 商品订单退款
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface RefundproductorderDao extends BaseMapper<RefundproductorderEntity> {
      
      List<RefundproductorderVO> selectListVO(@Param("ew") QueryWrapper<RefundproductorderEntity> wrapper);
      
      RefundproductorderVO selectVO(@Param("ew") QueryWrapper<RefundproductorderEntity> wrapper);
      
      List<RefundproductorderView> selectListView(@Param("ew") QueryWrapper<RefundproductorderEntity> wrapper);

      List<RefundproductorderView> selectListView(Page page, @Param("ew") QueryWrapper<RefundproductorderEntity> wrapper);

      RefundproductorderView selectView(@Param("ew") QueryWrapper<RefundproductorderEntity> wrapper);


  }
