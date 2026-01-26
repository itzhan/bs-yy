 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.FriendDao;
  import com.entity.FriendEntity;
  import com.service.FriendService;
  import com.entity.vo.FriendVO;
  import com.entity.view.FriendView;

  @Service("friendService")
  public class FriendServiceImpl extends ServiceImpl<FriendDao, FriendEntity> implements FriendService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<FriendEntity> page = this.page(
                  new Query<FriendEntity>(params).getPage(),
                  new QueryWrapper<FriendEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<FriendEntity> wrapper) {
          Page<FriendView> page = new Query<FriendView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<FriendVO> selectListVO(QueryWrapper<FriendEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public FriendVO selectVO(QueryWrapper<FriendEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<FriendView> selectListView(QueryWrapper<FriendEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public FriendView selectView(QueryWrapper<FriendEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
