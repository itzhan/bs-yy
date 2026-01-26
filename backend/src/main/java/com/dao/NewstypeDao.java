 package com.dao;

  import com.entity.NewstypeEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.NewstypeVO;
  import com.entity.view.NewstypeView;

  /**
   * 公告分类
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface NewstypeDao extends BaseMapper<NewstypeEntity> {
      
      List<NewstypeVO> selectListVO(@Param("ew") QueryWrapper<NewstypeEntity> wrapper);
      
      NewstypeVO selectVO(@Param("ew") QueryWrapper<NewstypeEntity> wrapper);
      
      List<NewstypeView> selectListView(@Param("ew") QueryWrapper<NewstypeEntity> wrapper);

      List<NewstypeView> selectListView(Page page, @Param("ew") QueryWrapper<NewstypeEntity> wrapper);

      NewstypeView selectView(@Param("ew") QueryWrapper<NewstypeEntity> wrapper);


  }
