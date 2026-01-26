 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.ShareEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.ShareVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.ShareView;

  /**
   * 会员交流
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface ShareService extends IService<ShareEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<ShareVO> selectListVO(QueryWrapper<ShareEntity> wrapper);
      
      ShareVO selectVO(@Param("ew") QueryWrapper<ShareEntity> wrapper);
      
      List<ShareView> selectListView(QueryWrapper<ShareEntity> wrapper);
      
      ShareView selectView(@Param("ew") QueryWrapper<ShareEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<ShareEntity> wrapper);


  }
