 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.ProductEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.ProductVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.ProductView;

  /**
   * 健身商品
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ProductService extends IService<ProductEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<ProductVO> selectListVO(QueryWrapper<ProductEntity> wrapper);
      
      ProductVO selectVO(@Param("ew") QueryWrapper<ProductEntity> wrapper);
      
      List<ProductView> selectListView(QueryWrapper<ProductEntity> wrapper);
      
      ProductView selectView(@Param("ew") QueryWrapper<ProductEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<ProductEntity> wrapper);


  }
