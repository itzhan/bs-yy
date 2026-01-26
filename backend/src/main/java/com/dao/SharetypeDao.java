 package com.dao;

  import com.entity.SharetypeEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.SharetypeVO;
  import com.entity.view.SharetypeView;

  /**
   * 交流分类
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface SharetypeDao extends BaseMapper<SharetypeEntity> {
      
      List<SharetypeVO> selectListVO(@Param("ew") QueryWrapper<SharetypeEntity> wrapper);
      
      SharetypeVO selectVO(@Param("ew") QueryWrapper<SharetypeEntity> wrapper);
      
      List<SharetypeView> selectListView(@Param("ew") QueryWrapper<SharetypeEntity> wrapper);

      List<SharetypeView> selectListView(Page page, @Param("ew") QueryWrapper<SharetypeEntity> wrapper);

      SharetypeView selectView(@Param("ew") QueryWrapper<SharetypeEntity> wrapper);


  }
