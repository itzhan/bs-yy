 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.MembershippackageDao;
  import com.entity.MembershippackageEntity;
  import com.service.MembershippackageService;
  import com.entity.vo.MembershippackageVO;
  import com.entity.view.MembershippackageView;

  @Service("membershippackageService")
  public class MembershippackageServiceImpl extends ServiceImpl<MembershippackageDao, MembershippackageEntity> implements MembershippackageService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<MembershippackageEntity> page = this.page(
                  new Query<MembershippackageEntity>(params).getPage(),
                  new QueryWrapper<MembershippackageEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<MembershippackageEntity> wrapper) {
          Page<MembershippackageView> page = new Query<MembershippackageView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<MembershippackageVO> selectListVO(QueryWrapper<MembershippackageEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public MembershippackageVO selectVO(QueryWrapper<MembershippackageEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<MembershippackageView> selectListView(QueryWrapper<MembershippackageEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public MembershippackageView selectView(QueryWrapper<MembershippackageEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
