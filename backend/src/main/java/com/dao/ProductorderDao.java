 package com.dao;

  import com.entity.ProductorderEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.ProductorderVO;
  import com.entity.view.ProductorderView;

  /**
   * 商品订单
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ProductorderDao extends BaseMapper<ProductorderEntity> {
      
      List<ProductorderVO> selectListVO(@Param("ew") QueryWrapper<ProductorderEntity> wrapper);
      
      ProductorderVO selectVO(@Param("ew") QueryWrapper<ProductorderEntity> wrapper);
      
      List<ProductorderView> selectListView(@Param("ew") QueryWrapper<ProductorderEntity> wrapper);

      List<ProductorderView> selectListView(Page page, @Param("ew") QueryWrapper<ProductorderEntity> wrapper);

      ProductorderView selectView(@Param("ew") QueryWrapper<ProductorderEntity> wrapper);


  }
