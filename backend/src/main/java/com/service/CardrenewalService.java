 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.CardrenewalEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.CardrenewalVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.CardrenewalView;

  /**
   * 续卡记录
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface CardrenewalService extends IService<CardrenewalEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<CardrenewalVO> selectListVO(QueryWrapper<CardrenewalEntity> wrapper);
      
      CardrenewalVO selectVO(@Param("ew") QueryWrapper<CardrenewalEntity> wrapper);
      
      List<CardrenewalView> selectListView(QueryWrapper<CardrenewalEntity> wrapper);
      
      CardrenewalView selectView(@Param("ew") QueryWrapper<CardrenewalEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<CardrenewalEntity> wrapper);


  }
