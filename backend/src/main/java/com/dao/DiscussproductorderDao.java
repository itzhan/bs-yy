 package com.dao;

  import com.entity.DiscussproductorderEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.DiscussproductorderVO;
  import com.entity.view.DiscussproductorderView;

  /**
   * 商品订单评论表
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface DiscussproductorderDao extends BaseMapper<DiscussproductorderEntity> {
      
      List<DiscussproductorderVO> selectListVO(@Param("ew") QueryWrapper<DiscussproductorderEntity> wrapper);
      
      DiscussproductorderVO selectVO(@Param("ew") QueryWrapper<DiscussproductorderEntity> wrapper);
      
      List<DiscussproductorderView> selectListView(@Param("ew") QueryWrapper<DiscussproductorderEntity> wrapper);

      List<DiscussproductorderView> selectListView(Page page, @Param("ew") QueryWrapper<DiscussproductorderEntity> wrapper);

      DiscussproductorderView selectView(@Param("ew") QueryWrapper<DiscussproductorderEntity> wrapper);


  }
