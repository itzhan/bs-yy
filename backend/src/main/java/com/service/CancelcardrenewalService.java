 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.CancelcardrenewalEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.CancelcardrenewalVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.CancelcardrenewalView;

  /**
   * 取消续卡记录
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CancelcardrenewalService extends IService<CancelcardrenewalEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<CancelcardrenewalVO> selectListVO(QueryWrapper<CancelcardrenewalEntity> wrapper);
      
      CancelcardrenewalVO selectVO(@Param("ew") QueryWrapper<CancelcardrenewalEntity> wrapper);
      
      List<CancelcardrenewalView> selectListView(QueryWrapper<CancelcardrenewalEntity> wrapper);
      
      CancelcardrenewalView selectView(@Param("ew") QueryWrapper<CancelcardrenewalEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<CancelcardrenewalEntity> wrapper);


  }
