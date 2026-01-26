 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.DiscussproductorderEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.DiscussproductorderVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.DiscussproductorderView;

  /**
   * 商品订单评论表
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface DiscussproductorderService extends IService<DiscussproductorderEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<DiscussproductorderVO> selectListVO(QueryWrapper<DiscussproductorderEntity> wrapper);
      
      DiscussproductorderVO selectVO(@Param("ew") QueryWrapper<DiscussproductorderEntity> wrapper);
      
      List<DiscussproductorderView> selectListView(QueryWrapper<DiscussproductorderEntity> wrapper);
      
      DiscussproductorderView selectView(@Param("ew") QueryWrapper<DiscussproductorderEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<DiscussproductorderEntity> wrapper);


  }
