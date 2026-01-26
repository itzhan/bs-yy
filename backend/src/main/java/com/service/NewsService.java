 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.NewsEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.NewsVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.NewsView;

  /**
   * 公告资讯
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface NewsService extends IService<NewsEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<NewsVO> selectListVO(QueryWrapper<NewsEntity> wrapper);
      
      NewsVO selectVO(@Param("ew") QueryWrapper<NewsEntity> wrapper);
      
      List<NewsView> selectListView(QueryWrapper<NewsEntity> wrapper);
      
      NewsView selectView(@Param("ew") QueryWrapper<NewsEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<NewsEntity> wrapper);


  }
