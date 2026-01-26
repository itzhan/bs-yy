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

import com.entity.ConfigEntity;
import com.entity.view.ConfigView;

import com.service.ConfigService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 轮播图
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/config")
public class ConfigController {
    @Resource
    private ConfigService configService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ConfigEntity config,
            HttpServletRequest request){
        QueryWrapper<ConfigEntity> ew = new QueryWrapper<ConfigEntity>();
        PageUtils page = configService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, config), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ConfigEntity config,
            HttpServletRequest request){
        QueryWrapper<ConfigEntity> ew = new QueryWrapper<ConfigEntity>();
        PageUtils page = configService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, config), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(ConfigEntity config){
        QueryWrapper<ConfigEntity> ew = new QueryWrapper<ConfigEntity>();
        ew.allEq(MPUtil.allEQMapPre(config, "config"));
        return R.ok().put("data", configService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(ConfigEntity config){
        QueryWrapper<ConfigEntity> ew = new QueryWrapper<ConfigEntity>();
        ew.allEq(MPUtil.allEQMapPre(config, "config"));
        ConfigView configView = configService.selectView(ew);
        return R.ok("查询轮播图成功").put("data", configView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ConfigEntity config = configService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(config,deSens);
        return R.ok().put("data", config);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ConfigEntity config = configService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(config,deSens);
        return R.ok().put("data", config);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ConfigEntity config, HttpServletRequest request){
        //ValidatorUtils.validateEntity(config);
        config.setId(null);
        configService.save(config);
        operationLogRecorder.record("config", "轮播图", "新增", config, request);
        return R.ok().put("data",config.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody ConfigEntity config, HttpServletRequest request){
        //ValidatorUtils.validateEntity(config);
        config.setId(null);
        configService.save(config);
        operationLogRecorder.record("config", "轮播图", "新增", config, request);
        return R.ok().put("data",config.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody ConfigEntity config, HttpServletRequest request){
        //ValidatorUtils.validateEntity(config);
        //全部更新
        configService.updateById(config);
        operationLogRecorder.record("config", "轮播图", "修改", config, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        configService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("config", "轮播图", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, ConfigEntity config, HttpServletRequest request, String pre){
        QueryWrapper<ConfigEntity> ew = new QueryWrapper<ConfigEntity>();
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
        PageUtils page = configService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, config), params), params));
        return R.ok().put("data", page);
    }





}
