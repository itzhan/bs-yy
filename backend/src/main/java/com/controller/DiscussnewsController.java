 package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.Set;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.IdUtil;

import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import com.utils.ValidatorUtils;
import com.utils.DeSensUtil;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.annotation.IgnoreAuth;

import com.entity.DiscussnewsEntity;
import com.entity.view.DiscussnewsView;

import com.service.DiscussnewsService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 公告资讯评论表
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/discussnews")
public class DiscussnewsController {
    @Resource
    private DiscussnewsService discussnewsService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, DiscussnewsEntity discussnews,
            HttpServletRequest request){
        QueryWrapper<DiscussnewsEntity> ew = new QueryWrapper<DiscussnewsEntity>();
        PageUtils page = discussnewsService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussnews), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, DiscussnewsEntity discussnews,
            HttpServletRequest request){
        QueryWrapper<DiscussnewsEntity> ew = new QueryWrapper<DiscussnewsEntity>();
        PageUtils page = discussnewsService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussnews), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(DiscussnewsEntity discussnews){
        QueryWrapper<DiscussnewsEntity> ew = new QueryWrapper<DiscussnewsEntity>();
        ew.allEq(MPUtil.allEQMapPre(discussnews, "discussnews"));
        return R.ok().put("data", discussnewsService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(DiscussnewsEntity discussnews){
        QueryWrapper<DiscussnewsEntity> ew = new QueryWrapper<DiscussnewsEntity>();
        ew.allEq(MPUtil.allEQMapPre(discussnews, "discussnews"));
        DiscussnewsView discussnewsView = discussnewsService.selectView(ew);
        return R.ok("查询公告资讯评论表成功").put("data", discussnewsView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DiscussnewsEntity discussnews = discussnewsService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(discussnews,deSens);
        return R.ok().put("data", discussnews);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DiscussnewsEntity discussnews = discussnewsService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(discussnews,deSens);
        return R.ok().put("data", discussnews);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody DiscussnewsEntity discussnews, HttpServletRequest request){
        //ValidatorUtils.validateEntity(discussnews);
        discussnews.setId(null);
        if (discussnews!= null && discussnews.getUserid() == null) {
            discussnews.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        discussnewsService.save(discussnews);
        operationLogRecorder.record("discussnews", "公告资讯评论表", "新增", discussnews, request);
        return R.ok().put("data",discussnews.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody DiscussnewsEntity discussnews, HttpServletRequest request){
        //ValidatorUtils.validateEntity(discussnews);
        discussnews.setId(null);
        if (discussnews!= null && discussnews.getUserid() == null) {
            discussnews.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        discussnewsService.save(discussnews);
        operationLogRecorder.record("discussnews", "公告资讯评论表", "新增", discussnews, request);
        return R.ok().put("data",discussnews.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody DiscussnewsEntity discussnews, HttpServletRequest request){
        //ValidatorUtils.validateEntity(discussnews);
        //全部更新
        discussnewsService.updateById(discussnews);
        operationLogRecorder.record("discussnews", "公告资讯评论表", "修改", discussnews, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        discussnewsService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("discussnews", "公告资讯评论表", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, DiscussnewsEntity discussnews, HttpServletRequest request, String pre){
        QueryWrapper<DiscussnewsEntity> ew = new QueryWrapper<DiscussnewsEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            String newKey = entry.getKey();
            if (pre.endsWith(".")) {
                newMap.put(pre + newKey, entry.getValue());
            } else if (StrUtil.isEmpty(pre)) {
                newMap.put(newKey, entry.getValue());
            } else {
                newMap.put(pre + "." + newKey, entry.getValue());
            }
        }
        params.put("sort", "clicktime");
        params.put("order", "desc");
        PageUtils page = discussnewsService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussnews), params), params));
        return R.ok().put("data", page);
    }





}
