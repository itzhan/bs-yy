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

import com.entity.RefundproductorderEntity;
import com.entity.view.RefundproductorderView;
import com.entity.ProductorderEntity;
import com.entity.ProductEntity;

import com.service.RefundproductorderService;
import com.service.ProductorderService;
import com.service.ProductService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 商品订单退款
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/refundproductorder")
public class RefundproductorderController {
    @Resource
    private RefundproductorderService refundproductorderService;
    @Resource
    private ProductorderService productorderService;
    @Resource
    private ProductService productService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, RefundproductorderEntity refundproductorder,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            refundproductorder.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        String tableName = request.getSession().getAttribute("tableName").toString();
        String username = (String)request.getSession().getAttribute("username");
        QueryWrapper<RefundproductorderEntity> ew = new QueryWrapper<RefundproductorderEntity>();
        PageUtils page = refundproductorderService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundproductorder), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, RefundproductorderEntity refundproductorder,
            HttpServletRequest request){
//        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
//            refundproductorder.setUserid((Long)request.getSession().getAttribute("userId"));
//        }
        QueryWrapper<RefundproductorderEntity> ew = new QueryWrapper<RefundproductorderEntity>();
        PageUtils page = refundproductorderService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundproductorder), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(RefundproductorderEntity refundproductorder){
        QueryWrapper<RefundproductorderEntity> ew = new QueryWrapper<RefundproductorderEntity>();
        ew.allEq(MPUtil.allEQMapPre(refundproductorder, "refundproductorder"));
        return R.ok().put("data", refundproductorderService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(RefundproductorderEntity refundproductorder){
        QueryWrapper<RefundproductorderEntity> ew = new QueryWrapper<RefundproductorderEntity>();
        ew.allEq(MPUtil.allEQMapPre(refundproductorder, "refundproductorder"));
        RefundproductorderView refundproductorderView = refundproductorderService.selectView(ew);
        return R.ok("查询商品订单退款成功").put("data", refundproductorderView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        RefundproductorderEntity refundproductorder = refundproductorderService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(refundproductorder,deSens);
        return R.ok().put("data", refundproductorder);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        RefundproductorderEntity refundproductorder = refundproductorderService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(refundproductorder,deSens);
        return R.ok().put("data", refundproductorder);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody RefundproductorderEntity refundproductorder, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundproductorder);
        refundproductorder.setId(null);
        refundproductorder.setAuditstatus("待审核");
        if (refundproductorder!= null && refundproductorder.getUserid() == null) {
            refundproductorder.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        refundproductorderService.save(refundproductorder);
        operationLogRecorder.record("refundproductorder", "商品订单退款", "新增", refundproductorder, request);
        return R.ok().put("data",refundproductorder.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody RefundproductorderEntity refundproductorder, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundproductorder);
        refundproductorder.setId(null);
        refundproductorder.setAuditstatus("待审核");
        if (refundproductorder!= null && refundproductorder.getUserid() == null) {
            refundproductorder.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        refundproductorderService.save(refundproductorder);
        operationLogRecorder.record("refundproductorder", "商品订单退款", "新增", refundproductorder, request);
        return R.ok().put("data",refundproductorder.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody RefundproductorderEntity refundproductorder, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundproductorder);
        //全部更新
        refundproductorderService.updateById(refundproductorder);
        operationLogRecorder.record("refundproductorder", "商品订单退款", "修改", refundproductorder, request);
        return R.ok();
    }

    /**
    * 退款审核
    */
    @RequestMapping("/audit")
    @Transactional
    public R audit(@RequestBody Map<String, Object> params, HttpServletRequest request){
        Long id = Long.parseLong(params.get("id").toString());
        String auditstatus = params.get("auditstatus").toString();
        RefundproductorderEntity refund = refundproductorderService.getById(id);
        if(refund == null) {
            return R.error("退款记录不存在");
        }
        refund.setAuditstatus(auditstatus);
        refundproductorderService.updateById(refund);
        if("已通过".equals(auditstatus) && refund.getCrossrefid() != null) {
            ProductorderEntity order = productorderService.getById(refund.getCrossrefid());
            if(order != null) {
                order.setOrderstatus("已退款");
                productorderService.updateById(order);
                // 恢复商品库存
                if(refund.getProductname() != null) {
                    QueryWrapper<ProductEntity> pw = new QueryWrapper<>();
                    pw.eq("productname", refund.getProductname());
                    ProductEntity product = productService.getOne(pw);
                    if(product != null && product.getStock() != null && refund.getQuantity() != null) {
                        product.setStock(product.getStock() + refund.getQuantity());
                        productService.updateById(product);
                    }
                }
            }
        }
        if("已拒绝".equals(auditstatus) && refund.getCrossrefid() != null) {
            ProductorderEntity order = productorderService.getById(refund.getCrossrefid());
            if(order != null) {
                order.setOrderstatus("已支付");
                productorderService.updateById(order);
            }
        }
        operationLogRecorder.record("refundproductorder", "商品订单退款", "审核:" + auditstatus, refund, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        refundproductorderService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("refundproductorder", "商品订单退款", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, RefundproductorderEntity refundproductorder, HttpServletRequest request, String pre){
        QueryWrapper<RefundproductorderEntity> ew = new QueryWrapper<RefundproductorderEntity>();
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
        PageUtils page = refundproductorderService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundproductorder), params), params));
        return R.ok().put("data", page);
    }





}
