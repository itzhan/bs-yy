 package com.dao;

  import com.entity.ShareEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.ShareVO;
  import com.entity.view.ShareView;

  /**
   * 会员交流
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ShareDao extends BaseMapper<ShareEntity> {
      
      List<ShareVO> selectListVO(@Param("ew") QueryWrapper<ShareEntity> wrapper);
      
      ShareVO selectVO(@Param("ew") QueryWrapper<ShareEntity> wrapper);
      
      List<ShareView> selectListView(@Param("ew") QueryWrapper<ShareEntity> wrapper);

      List<ShareView> selectListView(Page page, @Param("ew") QueryWrapper<ShareEntity> wrapper);

      ShareView selectView(@Param("ew") QueryWrapper<ShareEntity> wrapper);


  }
