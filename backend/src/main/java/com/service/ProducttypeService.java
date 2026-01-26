 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.ProducttypeEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.ProducttypeVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.ProducttypeView;

  /**
   * 商品分类
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ProducttypeService extends IService<ProducttypeEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<ProducttypeVO> selectListVO(QueryWrapper<ProducttypeEntity> wrapper);
      
      ProducttypeVO selectVO(@Param("ew") QueryWrapper<ProducttypeEntity> wrapper);
      
      List<ProducttypeView> selectListView(QueryWrapper<ProducttypeEntity> wrapper);
      
      ProducttypeView selectView(@Param("ew") QueryWrapper<ProducttypeEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<ProducttypeEntity> wrapper);


  }
