 package com.service;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.service.IService;
  import com.utils.PageUtils;
  import com.entity.MembershippackageEntity;
  import java.util.List;
  import java.util.Map;
  import com.entity.vo.MembershippackageVO;
  import org.apache.ibatis.annotations.Param;
  import com.entity.view.MembershippackageView;

  /**
   * 会员卡套餐
   *
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface MembershippackageService extends IService<MembershippackageEntity> {

      PageUtils queryPage(Map<String, Object> params);
      
      List<MembershippackageVO> selectListVO(QueryWrapper<MembershippackageEntity> wrapper);
      
      MembershippackageVO selectVO(@Param("ew") QueryWrapper<MembershippackageEntity> wrapper);
      
      List<MembershippackageView> selectListView(QueryWrapper<MembershippackageEntity> wrapper);
      
      MembershippackageView selectView(@Param("ew") QueryWrapper<MembershippackageEntity> wrapper);
      
      PageUtils queryPage(Map<String, Object> params, QueryWrapper<MembershippackageEntity> wrapper);


  }
