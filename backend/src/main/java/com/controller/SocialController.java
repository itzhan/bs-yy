 package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.Collections;
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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.annotation.IgnoreAuth;

import com.entity.SocialEntity;
import com.entity.view.SocialView;

import com.service.SocialService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;

/**
* 互动表
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-09-06 17:13:53
*/
@RestController
@RequestMapping("/social")
public class SocialController {
    @Resource
    private SocialService socialService;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, SocialEntity social,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            social.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        QueryWrapper<SocialEntity> ew = new QueryWrapper<SocialEntity>();
        PageUtils page = socialService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, social), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    //@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, SocialEntity social,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            social.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        QueryWrapper<SocialEntity> ew = new QueryWrapper<SocialEntity>();
        PageUtils page = socialService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, social), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(SocialEntity social){
        QueryWrapper<SocialEntity> ew = new QueryWrapper<SocialEntity>();
        ew.allEq(MPUtil.allEQMapPre(social, "social"));
        return R.ok().put("data", socialService.selectListView(ew));
    }

    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(SocialEntity social){
        QueryWrapper<SocialEntity> ew = new QueryWrapper<SocialEntity>();
        ew.allEq(MPUtil.allEQMapPre(social, "social"));
        SocialView socialView = socialService.selectView(ew);
        return R.ok("查询互动表成功").put("data", socialView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        SocialEntity social = socialService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(social,deSens);
        return R.ok().put("data", social);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        SocialEntity social = socialService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(social,deSens);
        return R.ok().put("data", social);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody SocialEntity social, HttpServletRequest request){
        //ValidatorUtils.validateEntity(social);
        social.setUserid((Long)request.getSession().getAttribute("userId"));
        socialService.save(social);
        return R.ok().put("data",social.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody SocialEntity social, HttpServletRequest request){
        //ValidatorUtils.validateEntity(social);
        social.setUserid((Long)request.getSession().getAttribute("userId"));
        socialService.save(social);
        return R.ok().put("data",social.getId());
    }

    /**
    * 获取用户密保
    */
    @IgnoreAuth
    @RequestMapping("/security")
    public R security(@RequestParam String username){
        SocialEntity social = socialService.getOne(new QueryWrapper<SocialEntity>().eq("", username));
        return R.ok().put("data", social);
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody SocialEntity social, HttpServletRequest request){
        //ValidatorUtils.validateEntity(social);
        //全部更新
        socialService.updateById(social);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        socialService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, SocialEntity social, HttpServletRequest request, String pre){
        QueryWrapper<SocialEntity> ew = new QueryWrapper<SocialEntity>();
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
        PageUtils page = socialService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, social), params), params));
        return R.ok().put("data", page);
    }
}