 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.SocialEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.SocialVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.SocialView;

  /**
   * 互动表
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-09-06 17:14:55
   */
  public interface SocialService extends IService<SocialEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<SocialVO> selectListVO(QueryWrapper<SocialEntity> wrapper);
      
      SocialVO selectVO(@Param("ew") QueryWrapper<SocialEntity> wrapper);
      
      List<SocialView> selectListView(QueryWrapper<SocialEntity> wrapper);
      
      SocialView selectView(@Param("ew") QueryWrapper<SocialEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<SocialEntity> wrapper);

  }