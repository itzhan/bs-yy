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

import com.entity.CardrenewalEntity;
import com.entity.view.CardrenewalView;

import com.service.CardrenewalService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.service.NotifyService;
import com.entity.NotifyEntity;
import com.log.OperationLogRecorder;

/**
* 续卡记录
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/cardrenewal")
public class CardrenewalController {
    @Resource
    private CardrenewalService cardrenewalService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CardrenewalEntity cardrenewal,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            cardrenewal.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        QueryWrapper<CardrenewalEntity> ew = new QueryWrapper<CardrenewalEntity>();
        PageUtils page = cardrenewalService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cardrenewal), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CardrenewalEntity cardrenewal,
            HttpServletRequest request){
//        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
//            cardrenewal.setUserid((Long)request.getSession().getAttribute("userId"));
//        }
        QueryWrapper<CardrenewalEntity> ew = new QueryWrapper<CardrenewalEntity>();
        PageUtils page = cardrenewalService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cardrenewal), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(CardrenewalEntity cardrenewal){
        QueryWrapper<CardrenewalEntity> ew = new QueryWrapper<CardrenewalEntity>();
        ew.allEq(MPUtil.allEQMapPre(cardrenewal, "cardrenewal"));
        return R.ok().put("data", cardrenewalService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(CardrenewalEntity cardrenewal){
        QueryWrapper<CardrenewalEntity> ew = new QueryWrapper<CardrenewalEntity>();
        ew.allEq(MPUtil.allEQMapPre(cardrenewal, "cardrenewal"));
        CardrenewalView cardrenewalView = cardrenewalService.selectView(ew);
        return R.ok("查询续卡记录成功").put("data", cardrenewalView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CardrenewalEntity cardrenewal = cardrenewalService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(cardrenewal,deSens);
        return R.ok().put("data", cardrenewal);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CardrenewalEntity cardrenewal = cardrenewalService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(cardrenewal,deSens);
        return R.ok().put("data", cardrenewal);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody CardrenewalEntity cardrenewal, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cardrenewal);
        cardrenewal.setId(null);
        if (cardrenewal!= null && cardrenewal.getUserid() == null) {
            cardrenewal.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        cardrenewalService.save(cardrenewal);
        {
            NotifyEntity nf_0 = new NotifyEntity();
            Long nf_0_userId = null;
            nf_0_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_0_userId == null) {
                nf_0_userId = 1L;
            }
            nf_0.setUserid(nf_0_userId);
            nf_0.setTitle(String.format("续卡成功", cardrenewal.getPackagename(), cardrenewal.getRenewaldays()));
            nf_0.setContent(String.format("您的会员卡续费成功，续费套餐：%s，续费时长：%s天", cardrenewal.getPackagename(), cardrenewal.getRenewaldays()));
            nf_0.setMessagetype("系统通知");
            nf_0.setReadstatus("未读");
            nf_0.setSenduser("系统");
            notifyService.save(nf_0);
        }
        operationLogRecorder.record("cardrenewal", "续卡记录", "新增", cardrenewal, request);
        return R.ok().put("data",cardrenewal.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody CardrenewalEntity cardrenewal, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cardrenewal);
        cardrenewal.setId(null);
        if (cardrenewal!= null && cardrenewal.getUserid() == null) {
            cardrenewal.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        cardrenewalService.save(cardrenewal);
        {
            NotifyEntity nf_0 = new NotifyEntity();
            Long nf_0_userId = null;
            nf_0_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_0_userId == null) {
                nf_0_userId = 1L;
            }
            nf_0.setUserid(nf_0_userId);
            nf_0.setTitle(String.format("续卡成功", cardrenewal.getPackagename(), cardrenewal.getRenewaldays()));
            nf_0.setContent(String.format("您的会员卡续费成功，续费套餐：%s，续费时长：%s天", cardrenewal.getPackagename(), cardrenewal.getRenewaldays()));
            nf_0.setMessagetype("系统通知");
            nf_0.setReadstatus("未读");
            nf_0.setSenduser("系统");
            notifyService.save(nf_0);
        }
        operationLogRecorder.record("cardrenewal", "续卡记录", "新增", cardrenewal, request);
        return R.ok().put("data",cardrenewal.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody CardrenewalEntity cardrenewal, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cardrenewal);
        //全部更新
        cardrenewalService.updateById(cardrenewal);
        {
            boolean _send = true;
            _send = "已支付".equals(String.valueOf(cardrenewal.getIspay()));
            if (_send) {
            NotifyEntity nf_1 = new NotifyEntity();
            Long nf_1_userId = null;
            nf_1_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_1_userId == null) {
                nf_1_userId = 1L;
            }
            nf_1.setUserid(nf_1_userId);
            nf_1.setTitle(String.format("续卡支付成功", cardrenewal.getPackagename(), cardrenewal.getRenewaldays()));
            nf_1.setContent(String.format("您的续卡订单已支付成功，套餐：%s，续费时长：%s天", cardrenewal.getPackagename(), cardrenewal.getRenewaldays()));
            nf_1.setMessagetype("支付通知");
            nf_1.setReadstatus("未读");
            nf_1.setSenduser("系统");
            notifyService.save(nf_1);
            }
        }
        operationLogRecorder.record("cardrenewal", "续卡记录", "修改", cardrenewal, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        cardrenewalService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("cardrenewal", "续卡记录", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, CardrenewalEntity cardrenewal, HttpServletRequest request, String pre){
        QueryWrapper<CardrenewalEntity> ew = new QueryWrapper<CardrenewalEntity>();
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
        PageUtils page = cardrenewalService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cardrenewal), params), params));
        return R.ok().put("data", page);
    }





}
