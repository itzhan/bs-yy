 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.ChatmessageDao;
  import com.entity.ChatmessageEntity;
  import com.service.ChatmessageService;
  import com.entity.vo.ChatmessageVO;
  import com.entity.view.ChatmessageView;

  @Service("chatmessageService")
  public class ChatmessageServiceImpl extends ServiceImpl<ChatmessageDao, ChatmessageEntity> implements ChatmessageService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<ChatmessageEntity> page = this.page(
                  new Query<ChatmessageEntity>(params).getPage(),
                  new QueryWrapper<ChatmessageEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ChatmessageEntity> wrapper) {
          Page<ChatmessageView> page = new Query<ChatmessageView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<ChatmessageVO> selectListVO(QueryWrapper<ChatmessageEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public ChatmessageVO selectVO(QueryWrapper<ChatmessageEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<ChatmessageView> selectListView(QueryWrapper<ChatmessageEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public ChatmessageView selectView(QueryWrapper<ChatmessageEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
