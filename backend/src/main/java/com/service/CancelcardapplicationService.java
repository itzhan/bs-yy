 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.CancelcardapplicationEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.CancelcardapplicationVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.CancelcardapplicationView;

  /**
   * 取消办卡记录
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CancelcardapplicationService extends IService<CancelcardapplicationEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<CancelcardapplicationVO> selectListVO(QueryWrapper<CancelcardapplicationEntity> wrapper);
      
      CancelcardapplicationVO selectVO(@Param("ew") QueryWrapper<CancelcardapplicationEntity> wrapper);
      
      List<CancelcardapplicationView> selectListView(QueryWrapper<CancelcardapplicationEntity> wrapper);
      
      CancelcardapplicationView selectView(@Param("ew") QueryWrapper<CancelcardapplicationEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<CancelcardapplicationEntity> wrapper);


  }
