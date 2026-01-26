 package com.dao;

  import com.entity.MembershippackageEntity;
  import com.baomidou.mybatisplus.core.mapper.BaseMapper;
  import java.util.List;
  import java.util.Map;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

  import org.apache.ibatis.annotations.Param;
  import com.entity.vo.MembershippackageVO;
  import com.entity.view.MembershippackageView;

  /**
   * 会员卡套餐
   * 
   * @author ds
   * @email 1312461553@qq.com
   * @date 2025-11-13 21:24:29
   */
  public interface MembershippackageDao extends BaseMapper<MembershippackageEntity> {
      
      List<MembershippackageVO> selectListVO(@Param("ew") QueryWrapper<MembershippackageEntity> wrapper);
      
      MembershippackageVO selectVO(@Param("ew") QueryWrapper<MembershippackageEntity> wrapper);
      
      List<MembershippackageView> selectListView(@Param("ew") QueryWrapper<MembershippackageEntity> wrapper);

      List<MembershippackageView> selectListView(Page page, @Param("ew") QueryWrapper<MembershippackageEntity> wrapper);

      MembershippackageView selectView(@Param("ew") QueryWrapper<MembershippackageEntity> wrapper);


  }
