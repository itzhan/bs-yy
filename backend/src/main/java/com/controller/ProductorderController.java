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

import com.entity.ProductorderEntity;
import com.entity.view.ProductorderView;

import com.service.ProductorderService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.service.NotifyService;
import com.entity.NotifyEntity;
import com.log.OperationLogRecorder;

/**
* 商品订单
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/productorder")
public class ProductorderController {
    @Resource
    private ProductorderService productorderService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ProductorderEntity productorder,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            productorder.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        QueryWrapper<ProductorderEntity> ew = new QueryWrapper<ProductorderEntity>();
        PageUtils page = productorderService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, productorder), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ProductorderEntity productorder,
            HttpServletRequest request){
//        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
//            productorder.setUserid((Long)request.getSession().getAttribute("userId"));
//        }
        QueryWrapper<ProductorderEntity> ew = new QueryWrapper<ProductorderEntity>();
        PageUtils page = productorderService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, productorder), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(ProductorderEntity productorder){
        QueryWrapper<ProductorderEntity> ew = new QueryWrapper<ProductorderEntity>();
        ew.allEq(MPUtil.allEQMapPre(productorder, "productorder"));
        return R.ok().put("data", productorderService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(ProductorderEntity productorder){
        QueryWrapper<ProductorderEntity> ew = new QueryWrapper<ProductorderEntity>();
        ew.allEq(MPUtil.allEQMapPre(productorder, "productorder"));
        ProductorderView productorderView = productorderService.selectView(ew);
        return R.ok("查询商品订单成功").put("data", productorderView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ProductorderEntity productorder = productorderService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(productorder,deSens);
        return R.ok().put("data", productorder);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ProductorderEntity productorder = productorderService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(productorder,deSens);
        return R.ok().put("data", productorder);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ProductorderEntity productorder, HttpServletRequest request){
        //ValidatorUtils.validateEntity(productorder);
        productorder.setId(null);
        if (productorder!= null && productorder.getUserid() == null) {
            productorder.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        productorderService.save(productorder);
        {
            NotifyEntity nf_0 = new NotifyEntity();
            Long nf_0_userId = null;
            nf_0_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_0_userId == null) {
                nf_0_userId = 1L;
            }
            nf_0.setUserid(nf_0_userId);
            nf_0.setTitle(String.format("订单提交成功", productorder.getProductname(), productorder.getTotalprice()));
            nf_0.setContent(String.format("您的商品订单《%s》已提交，订单金额：%s元", productorder.getProductname(), productorder.getTotalprice()));
            nf_0.setMessagetype("订单通知");
            nf_0.setReadstatus("未读");
            nf_0.setSenduser("系统");
            notifyService.save(nf_0);
        }
        operationLogRecorder.record("productorder", "商品订单", "新增", productorder, request);
        return R.ok().put("data",productorder.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody ProductorderEntity productorder, HttpServletRequest request){
        //ValidatorUtils.validateEntity(productorder);
        productorder.setId(null);
        if (productorder!= null && productorder.getUserid() == null) {
            productorder.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        productorderService.save(productorder);
        {
            NotifyEntity nf_0 = new NotifyEntity();
            Long nf_0_userId = null;
            nf_0_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_0_userId == null) {
                nf_0_userId = 1L;
            }
            nf_0.setUserid(nf_0_userId);
            nf_0.setTitle(String.format("订单提交成功", productorder.getProductname(), productorder.getTotalprice()));
            nf_0.setContent(String.format("您的商品订单《%s》已提交，订单金额：%s元", productorder.getProductname(), productorder.getTotalprice()));
            nf_0.setMessagetype("订单通知");
            nf_0.setReadstatus("未读");
            nf_0.setSenduser("系统");
            notifyService.save(nf_0);
        }
        operationLogRecorder.record("productorder", "商品订单", "新增", productorder, request);
        return R.ok().put("data",productorder.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody ProductorderEntity productorder, HttpServletRequest request){
        //ValidatorUtils.validateEntity(productorder);
        //全部更新
        productorderService.updateById(productorder);
        {
            boolean _send = true;
            _send = "已支付".equals(String.valueOf(productorder.getIspay()));
            if (_send) {
            NotifyEntity nf_1 = new NotifyEntity();
            Long nf_1_userId = null;
            nf_1_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_1_userId == null) {
                nf_1_userId = 1L;
            }
            nf_1.setUserid(nf_1_userId);
            nf_1.setTitle(String.format("支付成功", productorder.getProductname(), productorder.getTotalprice()));
            nf_1.setContent(String.format("您的商品订单《%s》支付成功，金额：%s元", productorder.getProductname(), productorder.getTotalprice()));
            nf_1.setMessagetype("订单通知");
            nf_1.setReadstatus("未读");
            nf_1.setSenduser("系统");
            notifyService.save(nf_1);
            }
        }
        {
            boolean _send = true;
            _send = "已发货".equals(String.valueOf(productorder.getOrderstatus()));
            if (_send) {
            NotifyEntity nf_2 = new NotifyEntity();
            Long nf_2_userId = null;
            nf_2_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_2_userId == null) {
                nf_2_userId = 1L;
            }
            nf_2.setUserid(nf_2_userId);
            nf_2.setTitle(String.format("订单已发货", productorder.getProductname()));
            nf_2.setContent(String.format("您的商品订单《%s》已发货，请留意物流信息", productorder.getProductname()));
            nf_2.setMessagetype("订单通知");
            nf_2.setReadstatus("未读");
            nf_2.setSenduser("系统");
            notifyService.save(nf_2);
            }
        }
        operationLogRecorder.record("productorder", "商品订单", "修改", productorder, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        productorderService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("productorder", "商品订单", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, ProductorderEntity productorder, HttpServletRequest request, String pre){
        QueryWrapper<ProductorderEntity> ew = new QueryWrapper<ProductorderEntity>();
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
        PageUtils page = productorderService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, productorder), params), params));
        return R.ok().put("data", page);
    }





}
