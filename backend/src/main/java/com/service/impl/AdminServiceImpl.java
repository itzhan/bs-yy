 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.AdminDao;
  import com.entity.AdminEntity;
  import com.service.AdminService;
  import com.entity.vo.AdminVO;
  import com.entity.view.AdminView;

  @Service("adminService")
  public class AdminServiceImpl extends ServiceImpl<AdminDao, AdminEntity> implements AdminService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<AdminEntity> page = this.page(
                  new Query<AdminEntity>(params).getPage(),
                  new QueryWrapper<AdminEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<AdminEntity> wrapper) {
          Page<AdminView> page = new Query<AdminView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<AdminVO> selectListVO(QueryWrapper<AdminEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public AdminVO selectVO(QueryWrapper<AdminEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<AdminView> selectListView(QueryWrapper<AdminEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public AdminView selectView(QueryWrapper<AdminEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
