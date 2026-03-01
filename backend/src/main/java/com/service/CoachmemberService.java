package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.CoachmemberEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.CoachmemberVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.CoachmemberView;

/**
 * 教练-会员绑定
 *
 * @author ds
 * @email 1312461553@qq.com
 */
public interface CoachmemberService extends IService<CoachmemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CoachmemberVO> selectListVO(QueryWrapper<CoachmemberEntity> wrapper);

    CoachmemberVO selectVO(@Param("ew") QueryWrapper<CoachmemberEntity> wrapper);

    List<CoachmemberView> selectListView(QueryWrapper<CoachmemberEntity> wrapper);

    CoachmemberView selectView(@Param("ew") QueryWrapper<CoachmemberEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<CoachmemberEntity> wrapper);
}
