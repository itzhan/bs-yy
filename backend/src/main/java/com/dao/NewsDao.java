 package com.dao;

  import com.entity.NewsEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.NewsVO;
  import com.entity.view.NewsView;

  /**
   * 公告资讯
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface NewsDao extends BaseMapper<NewsEntity> {
      
      List<NewsVO> selectListVO(@Param("ew") QueryWrapper<NewsEntity> wrapper);
      
      NewsVO selectVO(@Param("ew") QueryWrapper<NewsEntity> wrapper);
      
      List<NewsView> selectListView(@Param("ew") QueryWrapper<NewsEntity> wrapper);

      List<NewsView> selectListView(Page page, @Param("ew") QueryWrapper<NewsEntity> wrapper);

      NewsView selectView(@Param("ew") QueryWrapper<NewsEntity> wrapper);


  }
