 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.NewstypeEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.NewstypeVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.NewstypeView;

  /**
   * 公告分类
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface NewstypeService extends IService<NewstypeEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<NewstypeVO> selectListVO(QueryWrapper<NewstypeEntity> wrapper);
      
      NewstypeVO selectVO(@Param("ew") QueryWrapper<NewstypeEntity> wrapper);
      
      List<NewstypeView> selectListView(QueryWrapper<NewstypeEntity> wrapper);
      
      NewstypeView selectView(@Param("ew") QueryWrapper<NewstypeEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<NewstypeEntity> wrapper);


  }
