 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.RefundcardrenewalEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.RefundcardrenewalVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.RefundcardrenewalView;

  /**
   * 续卡记录退款
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface RefundcardrenewalService extends IService<RefundcardrenewalEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<RefundcardrenewalVO> selectListVO(QueryWrapper<RefundcardrenewalEntity> wrapper);
      
      RefundcardrenewalVO selectVO(@Param("ew") QueryWrapper<RefundcardrenewalEntity> wrapper);
      
      List<RefundcardrenewalView> selectListView(QueryWrapper<RefundcardrenewalEntity> wrapper);
      
      RefundcardrenewalView selectView(@Param("ew") QueryWrapper<RefundcardrenewalEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<RefundcardrenewalEntity> wrapper);


  }
