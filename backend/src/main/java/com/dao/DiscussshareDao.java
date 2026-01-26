 package com.dao;

  import com.entity.DiscussshareEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.DiscussshareVO;
  import com.entity.view.DiscussshareView;

  /**
   * 会员交流评论表
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface DiscussshareDao extends BaseMapper<DiscussshareEntity> {
      
      List<DiscussshareVO> selectListVO(@Param("ew") QueryWrapper<DiscussshareEntity> wrapper);
      
      DiscussshareVO selectVO(@Param("ew") QueryWrapper<DiscussshareEntity> wrapper);
      
      List<DiscussshareView> selectListView(@Param("ew") QueryWrapper<DiscussshareEntity> wrapper);

      List<DiscussshareView> selectListView(Page page, @Param("ew") QueryWrapper<DiscussshareEntity> wrapper);

      DiscussshareView selectView(@Param("ew") QueryWrapper<DiscussshareEntity> wrapper);


  }
