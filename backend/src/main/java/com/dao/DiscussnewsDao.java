 package com.dao;

  import com.entity.DiscussnewsEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.DiscussnewsVO;
  import com.entity.view.DiscussnewsView;

  /**
   * 公告资讯评论表
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface DiscussnewsDao extends BaseMapper<DiscussnewsEntity> {
      
      List<DiscussnewsVO> selectListVO(@Param("ew") QueryWrapper<DiscussnewsEntity> wrapper);
      
      DiscussnewsVO selectVO(@Param("ew") QueryWrapper<DiscussnewsEntity> wrapper);
      
      List<DiscussnewsView> selectListView(@Param("ew") QueryWrapper<DiscussnewsEntity> wrapper);

      List<DiscussnewsView> selectListView(Page page, @Param("ew") QueryWrapper<DiscussnewsEntity> wrapper);

      DiscussnewsView selectView(@Param("ew") QueryWrapper<DiscussnewsEntity> wrapper);


  }
