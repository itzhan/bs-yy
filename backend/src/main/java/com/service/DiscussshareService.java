 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.DiscussshareEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.DiscussshareVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.DiscussshareView;

  /**
   * 会员交流评论表
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface DiscussshareService extends IService<DiscussshareEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<DiscussshareVO> selectListVO(QueryWrapper<DiscussshareEntity> wrapper);
      
      DiscussshareVO selectVO(@Param("ew") QueryWrapper<DiscussshareEntity> wrapper);
      
      List<DiscussshareView> selectListView(QueryWrapper<DiscussshareEntity> wrapper);
      
      DiscussshareView selectView(@Param("ew") QueryWrapper<DiscussshareEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<DiscussshareEntity> wrapper);


  }
