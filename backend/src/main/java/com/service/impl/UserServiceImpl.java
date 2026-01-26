 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.UserDao;
  import com.entity.UserEntity;
  import com.service.UserService;
  import com.entity.vo.UserVO;
  import com.entity.view.UserView;

  @Service("userService")
  public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<UserEntity> page = this.page(
                  new Query<UserEntity>(params).getPage(),
                  new QueryWrapper<UserEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<UserEntity> wrapper) {
          Page<UserView> page = new Query<UserView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<UserVO> selectListVO(QueryWrapper<UserEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public UserVO selectVO(QueryWrapper<UserEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<UserView> selectListView(QueryWrapper<UserEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public UserView selectView(QueryWrapper<UserEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
