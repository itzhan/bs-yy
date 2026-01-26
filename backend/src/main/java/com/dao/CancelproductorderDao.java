 package com.dao;

  import com.entity.CancelproductorderEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.CancelproductorderVO;
  import com.entity.view.CancelproductorderView;

  /**
   * 取消商品订单
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CancelproductorderDao extends BaseMapper<CancelproductorderEntity> {
      
      List<CancelproductorderVO> selectListVO(@Param("ew") QueryWrapper<CancelproductorderEntity> wrapper);
      
      CancelproductorderVO selectVO(@Param("ew") QueryWrapper<CancelproductorderEntity> wrapper);
      
      List<CancelproductorderView> selectListView(@Param("ew") QueryWrapper<CancelproductorderEntity> wrapper);

      List<CancelproductorderView> selectListView(Page page, @Param("ew") QueryWrapper<CancelproductorderEntity> wrapper);

      CancelproductorderView selectView(@Param("ew") QueryWrapper<CancelproductorderEntity> wrapper);


  }
