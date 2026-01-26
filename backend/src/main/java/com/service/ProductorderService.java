 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.ProductorderEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.ProductorderVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.ProductorderView;

  /**
   * 商品订单
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ProductorderService extends IService<ProductorderEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<ProductorderVO> selectListVO(QueryWrapper<ProductorderEntity> wrapper);
      
      ProductorderVO selectVO(@Param("ew") QueryWrapper<ProductorderEntity> wrapper);
      
      List<ProductorderView> selectListView(QueryWrapper<ProductorderEntity> wrapper);
      
      ProductorderView selectView(@Param("ew") QueryWrapper<ProductorderEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<ProductorderEntity> wrapper);


  }
