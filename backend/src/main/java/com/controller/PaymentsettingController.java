package com.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.annotation.IgnoreAuth;

import com.entity.PaymentsettingEntity;
import com.entity.view.PaymentsettingView;
import com.service.PaymentsettingService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.DeSensUtil;
import com.log.OperationLogRecorder;

/**
 * 支付设置
 * 后端接口
 * @author ds
 */
@RestController
@RequestMapping("/paymentsetting")
public class PaymentsettingController {
    @Resource
    private PaymentsettingService paymentsettingService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, PaymentsettingEntity paymentsetting,
            HttpServletRequest request){
        QueryWrapper<PaymentsettingEntity> ew = new QueryWrapper<PaymentsettingEntity>();
        PageUtils page = paymentsettingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, paymentsetting), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 前台列表（公开接口，用于用户端获取启用的支付方式）
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, PaymentsettingEntity paymentsetting,
            HttpServletRequest request){
        QueryWrapper<PaymentsettingEntity> ew = new QueryWrapper<PaymentsettingEntity>();
        PageUtils page = paymentsettingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, paymentsetting), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        PaymentsettingEntity paymentsetting = paymentsettingService.getById(id);
        return R.ok().put("data", paymentsetting);
    }

    /**
     * 前台详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        PaymentsettingEntity paymentsetting = paymentsettingService.getById(id);
        return R.ok().put("data", paymentsetting);
    }

    /**
     * 后台保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PaymentsettingEntity paymentsetting, HttpServletRequest request){
        paymentsetting.setId(null);
        paymentsettingService.save(paymentsetting);
        operationLogRecorder.record("paymentsetting", "支付设置", "新增", paymentsetting, request);
        return R.ok().put("data", paymentsetting.getId());
    }

    /**
     * 前台保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody PaymentsettingEntity paymentsetting, HttpServletRequest request){
        paymentsetting.setId(null);
        paymentsettingService.save(paymentsetting);
        operationLogRecorder.record("paymentsetting", "支付设置", "新增", paymentsetting, request);
        return R.ok().put("data", paymentsetting.getId());
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PaymentsettingEntity paymentsetting, HttpServletRequest request){
        paymentsettingService.updateById(paymentsetting);
        operationLogRecorder.record("paymentsetting", "支付设置", "修改", paymentsetting, request);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        paymentsettingService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("paymentsetting", "支付设置", "删除", Arrays.asList(ids), request);
        return R.ok();
    }
}
