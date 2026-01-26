 package com.dao;

  import com.entity.SocialEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.SocialVO;
  import com.entity.view.SocialView;

  /**
   * 互动表
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-09-06 17:14:08
   */
  public interface SocialDao extends BaseMapper<SocialEntity> {
      
      List<SocialVO> selectListVO(@Param("ew") QueryWrapper<SocialEntity> wrapper);
      
      SocialVO selectVO(@Param("ew") QueryWrapper<SocialEntity> wrapper);
      
      List<SocialView> selectListView(@Param("ew") QueryWrapper<SocialEntity> wrapper);

      List<SocialView> selectListView(Page page, @Param("ew") QueryWrapper<SocialEntity> wrapper);

      SocialView selectView(@Param("ew") QueryWrapper<SocialEntity> wrapper);

  }