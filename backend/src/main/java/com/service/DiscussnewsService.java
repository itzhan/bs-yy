 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.DiscussnewsEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.DiscussnewsVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.DiscussnewsView;

  /**
   * 公告资讯评论表
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface DiscussnewsService extends IService<DiscussnewsEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<DiscussnewsVO> selectListVO(QueryWrapper<DiscussnewsEntity> wrapper);
      
      DiscussnewsVO selectVO(@Param("ew") QueryWrapper<DiscussnewsEntity> wrapper);
      
      List<DiscussnewsView> selectListView(QueryWrapper<DiscussnewsEntity> wrapper);
      
      DiscussnewsView selectView(@Param("ew") QueryWrapper<DiscussnewsEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<DiscussnewsEntity> wrapper);


  }
