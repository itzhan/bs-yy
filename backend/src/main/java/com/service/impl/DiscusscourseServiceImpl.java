 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.DiscusscourseDao;
  import com.entity.DiscusscourseEntity;
  import com.service.DiscusscourseService;
  import com.entity.vo.DiscusscourseVO;
  import com.entity.view.DiscusscourseView;

  @Service("discusscourseService")
  public class DiscusscourseServiceImpl extends ServiceImpl<DiscusscourseDao, DiscusscourseEntity> implements DiscusscourseService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<DiscusscourseEntity> page = this.page(
                  new Query<DiscusscourseEntity>(params).getPage(),
                  new QueryWrapper<DiscusscourseEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<DiscusscourseEntity> wrapper) {
          Page<DiscusscourseView> page = new Query<DiscusscourseView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<DiscusscourseVO> selectListVO(QueryWrapper<DiscusscourseEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public DiscusscourseVO selectVO(QueryWrapper<DiscusscourseEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<DiscusscourseView> selectListView(QueryWrapper<DiscusscourseEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public DiscusscourseView selectView(QueryWrapper<DiscusscourseEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
