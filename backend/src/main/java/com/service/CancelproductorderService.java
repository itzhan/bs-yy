 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.CancelproductorderEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.CancelproductorderVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.CancelproductorderView;

  /**
   * 取消商品订单
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CancelproductorderService extends IService<CancelproductorderEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<CancelproductorderVO> selectListVO(QueryWrapper<CancelproductorderEntity> wrapper);
      
      CancelproductorderVO selectVO(@Param("ew") QueryWrapper<CancelproductorderEntity> wrapper);
      
      List<CancelproductorderView> selectListView(QueryWrapper<CancelproductorderEntity> wrapper);
      
      CancelproductorderView selectView(@Param("ew") QueryWrapper<CancelproductorderEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<CancelproductorderEntity> wrapper);


  }
