 package com.dao;

  import com.entity.DiscussproductEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.DiscussproductVO;
  import com.entity.view.DiscussproductView;

  /**
   * 健身商品评论表
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface DiscussproductDao extends BaseMapper<DiscussproductEntity> {
      
      List<DiscussproductVO> selectListVO(@Param("ew") QueryWrapper<DiscussproductEntity> wrapper);
      
      DiscussproductVO selectVO(@Param("ew") QueryWrapper<DiscussproductEntity> wrapper);
      
      List<DiscussproductView> selectListView(@Param("ew") QueryWrapper<DiscussproductEntity> wrapper);

      List<DiscussproductView> selectListView(Page page, @Param("ew") QueryWrapper<DiscussproductEntity> wrapper);

      DiscussproductView selectView(@Param("ew") QueryWrapper<DiscussproductEntity> wrapper);


  }
