 package com.dao;

  import com.entity.ProducttypeEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.ProducttypeVO;
  import com.entity.view.ProducttypeView;

  /**
   * 商品分类
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ProducttypeDao extends BaseMapper<ProducttypeEntity> {
      
      List<ProducttypeVO> selectListVO(@Param("ew") QueryWrapper<ProducttypeEntity> wrapper);
      
      ProducttypeVO selectVO(@Param("ew") QueryWrapper<ProducttypeEntity> wrapper);
      
      List<ProducttypeView> selectListView(@Param("ew") QueryWrapper<ProducttypeEntity> wrapper);

      List<ProducttypeView> selectListView(Page page, @Param("ew") QueryWrapper<ProducttypeEntity> wrapper);

      ProducttypeView selectView(@Param("ew") QueryWrapper<ProducttypeEntity> wrapper);


  }
