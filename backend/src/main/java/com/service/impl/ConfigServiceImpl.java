 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.ConfigDao;
  import com.entity.ConfigEntity;
  import com.service.ConfigService;
  import com.entity.vo.ConfigVO;
  import com.entity.view.ConfigView;

  @Service("configService")
  public class ConfigServiceImpl extends ServiceImpl<ConfigDao, ConfigEntity> implements ConfigService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<ConfigEntity> page = this.page(
                  new Query<ConfigEntity>(params).getPage(),
                  new QueryWrapper<ConfigEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ConfigEntity> wrapper) {
          Page<ConfigView> page = new Query<ConfigView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<ConfigVO> selectListVO(QueryWrapper<ConfigEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public ConfigVO selectVO(QueryWrapper<ConfigEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<ConfigView> selectListView(QueryWrapper<ConfigEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public ConfigView selectView(QueryWrapper<ConfigEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
