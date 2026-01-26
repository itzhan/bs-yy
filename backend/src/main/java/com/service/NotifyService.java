 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.NotifyEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.NotifyVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.NotifyView;

  /**
   * 消息通知
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface NotifyService extends IService<NotifyEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<NotifyVO> selectListVO(QueryWrapper<NotifyEntity> wrapper);
      
      NotifyVO selectVO(@Param("ew") QueryWrapper<NotifyEntity> wrapper);
      
      List<NotifyView> selectListView(QueryWrapper<NotifyEntity> wrapper);
      
      NotifyView selectView(@Param("ew") QueryWrapper<NotifyEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<NotifyEntity> wrapper);


  }
