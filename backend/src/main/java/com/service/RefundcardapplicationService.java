 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.RefundcardapplicationEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.RefundcardapplicationVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.RefundcardapplicationView;

  /**
   * 办卡记录退款
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface RefundcardapplicationService extends IService<RefundcardapplicationEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<RefundcardapplicationVO> selectListVO(QueryWrapper<RefundcardapplicationEntity> wrapper);
      
      RefundcardapplicationVO selectVO(@Param("ew") QueryWrapper<RefundcardapplicationEntity> wrapper);
      
      List<RefundcardapplicationView> selectListView(QueryWrapper<RefundcardapplicationEntity> wrapper);
      
      RefundcardapplicationView selectView(@Param("ew") QueryWrapper<RefundcardapplicationEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<RefundcardapplicationEntity> wrapper);


  }
