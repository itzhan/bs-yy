 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.CourseenrollmentDao;
  import com.entity.CourseenrollmentEntity;
  import com.service.CourseenrollmentService;
  import com.entity.vo.CourseenrollmentVO;
  import com.entity.view.CourseenrollmentView;

  @Service("courseenrollmentService")
  public class CourseenrollmentServiceImpl extends ServiceImpl<CourseenrollmentDao, CourseenrollmentEntity> implements CourseenrollmentService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<CourseenrollmentEntity> page = this.page(
                  new Query<CourseenrollmentEntity>(params).getPage(),
                  new QueryWrapper<CourseenrollmentEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CourseenrollmentEntity> wrapper) {
          Page<CourseenrollmentView> page = new Query<CourseenrollmentView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<CourseenrollmentVO> selectListVO(QueryWrapper<CourseenrollmentEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public CourseenrollmentVO selectVO(QueryWrapper<CourseenrollmentEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<CourseenrollmentView> selectListView(QueryWrapper<CourseenrollmentEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public CourseenrollmentView selectView(QueryWrapper<CourseenrollmentEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
