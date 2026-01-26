 package com.dao;

  import com.entity.ProductEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.ProductVO;
  import com.entity.view.ProductView;

  /**
   * 健身商品
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ProductDao extends BaseMapper<ProductEntity> {
      
      List<ProductVO> selectListVO(@Param("ew") QueryWrapper<ProductEntity> wrapper);
      
      ProductVO selectVO(@Param("ew") QueryWrapper<ProductEntity> wrapper);
      
      List<ProductView> selectListView(@Param("ew") QueryWrapper<ProductEntity> wrapper);

      List<ProductView> selectListView(Page page, @Param("ew") QueryWrapper<ProductEntity> wrapper);

      ProductView selectView(@Param("ew") QueryWrapper<ProductEntity> wrapper);


  }
