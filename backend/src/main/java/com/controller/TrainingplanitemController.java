 package com.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.annotation.IgnoreAuth;
import com.entity.TrainingplanitemEntity;
import com.entity.TrainingplanEntity;
import com.entity.view.TrainingplanitemView;
import com.service.TrainingplanitemService;
import com.service.TrainingplanService;
import com.utils.DeSensUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import com.log.OperationLogRecorder;

/**
* 训练计划明细
* 后端接口
* @author ds
* @email 1312461553@qq.com
*/
@RestController
@RequestMapping("/trainingplanitem")
public class TrainingplanitemController {
    @Resource
    private TrainingplanitemService trainingplanitemService;
    @Resource
    private TrainingplanService trainingplanService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, TrainingplanitemEntity trainingplanitem,
            HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object tableObj = request.getSession().getAttribute("tableName");
        if(roleObj != null && !"管理员".equals(roleObj.toString())) {
            String tableName = tableObj == null ? "" : tableObj.toString();
            if("coach".equals(tableName)) {
                trainingplanitem.setCoachid((Long)request.getSession().getAttribute("userId"));
            } else if("user".equals(tableName)) {
                trainingplanitem.setUserid((Long)request.getSession().getAttribute("userId"));
            }
        }
        QueryWrapper<TrainingplanitemEntity> ew = new QueryWrapper<TrainingplanitemEntity>();
        PageUtils page = trainingplanitemService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, trainingplanitem), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, TrainingplanitemEntity trainingplanitem,
            HttpServletRequest request){
        QueryWrapper<TrainingplanitemEntity> ew = new QueryWrapper<TrainingplanitemEntity>();
        PageUtils page = trainingplanitemService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, trainingplanitem), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(TrainingplanitemEntity trainingplanitem){
        QueryWrapper<TrainingplanitemEntity> ew = new QueryWrapper<TrainingplanitemEntity>();
        ew.allEq(MPUtil.allEQMapPre(trainingplanitem, "trainingplanitem"));
        return R.ok().put("data", trainingplanitemService.selectListView(ew));
    }

    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(TrainingplanitemEntity trainingplanitem){
        QueryWrapper<TrainingplanitemEntity> ew = new QueryWrapper<TrainingplanitemEntity>();
        ew.allEq(MPUtil.allEQMapPre(trainingplanitem, "trainingplanitem"));
        TrainingplanitemView trainingplanitemView = trainingplanitemService.selectView(ew);
        return R.ok("查询训练计划明细成功").put("data", trainingplanitemView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TrainingplanitemEntity trainingplanitem = trainingplanitemService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(trainingplanitem,deSens);
        return R.ok().put("data", trainingplanitem);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        TrainingplanitemEntity trainingplanitem = trainingplanitemService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(trainingplanitem,deSens);
        return R.ok().put("data", trainingplanitem);
    }

    private void fillPlanInfo(TrainingplanitemEntity trainingplanitem) {
        if (trainingplanitem == null || trainingplanitem.getPlanid() == null) {
            return;
        }
        TrainingplanEntity plan = trainingplanService.getById(trainingplanitem.getPlanid());
        if (plan != null) {
            if (StrUtil.isBlank(trainingplanitem.getPlanname())) {
                trainingplanitem.setPlanname(plan.getPlanname());
            }
            if (trainingplanitem.getUserid() == null) {
                trainingplanitem.setUserid(plan.getUserid());
            }
            if (trainingplanitem.getCoachid() == null) {
                trainingplanitem.setCoachid(plan.getCoachid());
            }
        }
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody TrainingplanitemEntity trainingplanitem, HttpServletRequest request){
        trainingplanitem.setId(null);
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("user".equals(tableName)) {
            return R.error("训练计划明细由教练维护，用户不可新增");
        }
        if (trainingplanitem != null && tableObj != null) {
            if ("user".equals(tableName) && trainingplanitem.getUserid() == null) {
                trainingplanitem.setUserid((Long)request.getSession().getAttribute("userId"));
            }
            if ("coach".equals(tableName) && trainingplanitem.getCoachid() == null) {
                trainingplanitem.setCoachid((Long)request.getSession().getAttribute("userId"));
            }
        }
        fillPlanInfo(trainingplanitem);
        trainingplanitemService.save(trainingplanitem);
        operationLogRecorder.record("trainingplanitem", "训练计划明细", "新增", trainingplanitem, request);
        return R.ok().put("data",trainingplanitem.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody TrainingplanitemEntity trainingplanitem, HttpServletRequest request){
        trainingplanitem.setId(null);
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("user".equals(tableName)) {
            return R.error("训练计划明细由教练维护，用户不可新增");
        }
        if (trainingplanitem != null && tableObj != null) {
            if ("user".equals(tableName) && trainingplanitem.getUserid() == null) {
                trainingplanitem.setUserid((Long)request.getSession().getAttribute("userId"));
            }
            if ("coach".equals(tableName) && trainingplanitem.getCoachid() == null) {
                trainingplanitem.setCoachid((Long)request.getSession().getAttribute("userId"));
            }
        }
        fillPlanInfo(trainingplanitem);
        trainingplanitemService.save(trainingplanitem);
        operationLogRecorder.record("trainingplanitem", "训练计划明细", "新增", trainingplanitem, request);
        return R.ok().put("data",trainingplanitem.getId());
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody TrainingplanitemEntity trainingplanitem, HttpServletRequest request){
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("user".equals(tableName)) {
            return R.error("训练计划明细由教练维护，用户不可修改");
        }
        trainingplanitemService.updateById(trainingplanitem);
        operationLogRecorder.record("trainingplanitem", "训练计划明细", "修改", trainingplanitem, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("user".equals(tableName)) {
            return R.error("训练计划明细由教练维护，用户不可删除");
        }
        trainingplanitemService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("trainingplanitem", "训练计划明细", "删除", Arrays.asList(ids), request);
        return R.ok();
    }
}
