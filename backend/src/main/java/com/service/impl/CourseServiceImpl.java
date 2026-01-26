 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.CourseDao;
  import com.entity.CourseEntity;
  import com.service.CourseService;
  import com.entity.vo.CourseVO;
  import com.entity.view.CourseView;

  @Service("courseService")
  public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity> implements CourseService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<CourseEntity> page = this.page(
                  new Query<CourseEntity>(params).getPage(),
                  new QueryWrapper<CourseEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CourseEntity> wrapper) {
          Page<CourseView> page = new Query<CourseView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<CourseVO> selectListVO(QueryWrapper<CourseEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public CourseVO selectVO(QueryWrapper<CourseEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<CourseView> selectListView(QueryWrapper<CourseEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public CourseView selectView(QueryWrapper<CourseEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
