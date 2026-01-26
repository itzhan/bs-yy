 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.SharetypeDao;
  import com.entity.SharetypeEntity;
  import com.service.SharetypeService;
  import com.entity.vo.SharetypeVO;
  import com.entity.view.SharetypeView;

  @Service("sharetypeService")
  public class SharetypeServiceImpl extends ServiceImpl<SharetypeDao, SharetypeEntity> implements SharetypeService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<SharetypeEntity> page = this.page(
                  new Query<SharetypeEntity>(params).getPage(),
                  new QueryWrapper<SharetypeEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<SharetypeEntity> wrapper) {
          Page<SharetypeView> page = new Query<SharetypeView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<SharetypeVO> selectListVO(QueryWrapper<SharetypeEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public SharetypeVO selectVO(QueryWrapper<SharetypeEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<SharetypeView> selectListView(QueryWrapper<SharetypeEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public SharetypeView selectView(QueryWrapper<SharetypeEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
