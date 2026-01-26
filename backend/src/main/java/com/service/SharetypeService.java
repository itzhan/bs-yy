 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.SharetypeEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.SharetypeVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.SharetypeView;

  /**
   * 交流分类
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface SharetypeService extends IService<SharetypeEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<SharetypeVO> selectListVO(QueryWrapper<SharetypeEntity> wrapper);
      
      SharetypeVO selectVO(@Param("ew") QueryWrapper<SharetypeEntity> wrapper);
      
      List<SharetypeView> selectListView(QueryWrapper<SharetypeEntity> wrapper);
      
      SharetypeView selectView(@Param("ew") QueryWrapper<SharetypeEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<SharetypeEntity> wrapper);


  }
