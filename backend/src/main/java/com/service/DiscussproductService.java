 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.DiscussproductEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.DiscussproductVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.DiscussproductView;

  /**
   * 健身商品评论表
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface DiscussproductService extends IService<DiscussproductEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<DiscussproductVO> selectListVO(QueryWrapper<DiscussproductEntity> wrapper);
      
      DiscussproductVO selectVO(@Param("ew") QueryWrapper<DiscussproductEntity> wrapper);
      
      List<DiscussproductView> selectListView(QueryWrapper<DiscussproductEntity> wrapper);
      
      DiscussproductView selectView(@Param("ew") QueryWrapper<DiscussproductEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<DiscussproductEntity> wrapper);


  }
