 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.RefundproductorderEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.RefundproductorderVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.RefundproductorderView;

  /**
   * 商品订单退款
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface RefundproductorderService extends IService<RefundproductorderEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<RefundproductorderVO> selectListVO(QueryWrapper<RefundproductorderEntity> wrapper);
      
      RefundproductorderVO selectVO(@Param("ew") QueryWrapper<RefundproductorderEntity> wrapper);
      
      List<RefundproductorderView> selectListView(QueryWrapper<RefundproductorderEntity> wrapper);
      
      RefundproductorderView selectView(@Param("ew") QueryWrapper<RefundproductorderEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<RefundproductorderEntity> wrapper);


  }
