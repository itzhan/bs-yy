 package com.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.annotation.IgnoreAuth;
import com.entity.TrainingrecordEntity;
import com.entity.TrainingplanEntity;
import com.entity.view.TrainingrecordView;
import com.service.TrainingrecordService;
import com.service.TrainingplanService;
import com.utils.DeSensUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import com.log.OperationLogRecorder;

/**
* 训练记录
* 后端接口
* @author ds
* @email 1312461553@qq.com
*/
@RestController
@RequestMapping("/trainingrecord")
public class TrainingrecordController {
    @Resource
    private TrainingrecordService trainingrecordService;
    @Resource
    private TrainingplanService trainingplanService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, TrainingrecordEntity trainingrecord,
            HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object tableObj = request.getSession().getAttribute("tableName");
        if(roleObj != null && !"管理员".equals(roleObj.toString())) {
            String tableName = tableObj == null ? "" : tableObj.toString();
            if("coach".equals(tableName)) {
                trainingrecord.setCoachid((Long)request.getSession().getAttribute("userId"));
            } else if("user".equals(tableName)) {
                trainingrecord.setUserid((Long)request.getSession().getAttribute("userId"));
            }
        }
        QueryWrapper<TrainingrecordEntity> ew = new QueryWrapper<TrainingrecordEntity>();
        PageUtils page = trainingrecordService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, trainingrecord), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, TrainingrecordEntity trainingrecord,
            HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object tableObj = request.getSession().getAttribute("tableName");
        if(roleObj != null && !"管理员".equals(roleObj.toString())) {
            String tableName = tableObj == null ? "" : tableObj.toString();
            if("coach".equals(tableName)) {
                trainingrecord.setCoachid((Long)request.getSession().getAttribute("userId"));
            } else if("user".equals(tableName)) {
                trainingrecord.setUserid((Long)request.getSession().getAttribute("userId"));
            }
        }
        QueryWrapper<TrainingrecordEntity> ew = new QueryWrapper<TrainingrecordEntity>();
        PageUtils page = trainingrecordService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, trainingrecord), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(TrainingrecordEntity trainingrecord){
        QueryWrapper<TrainingrecordEntity> ew = new QueryWrapper<TrainingrecordEntity>();
        ew.allEq(MPUtil.allEQMapPre(trainingrecord, "trainingrecord"));
        return R.ok().put("data", trainingrecordService.selectListView(ew));
    }

    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(TrainingrecordEntity trainingrecord){
        QueryWrapper<TrainingrecordEntity> ew = new QueryWrapper<TrainingrecordEntity>();
        ew.allEq(MPUtil.allEQMapPre(trainingrecord, "trainingrecord"));
        TrainingrecordView trainingrecordView = trainingrecordService.selectView(ew);
        return R.ok("查询训练记录成功").put("data", trainingrecordView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TrainingrecordEntity trainingrecord = trainingrecordService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(trainingrecord,deSens);
        return R.ok().put("data", trainingrecord);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        TrainingrecordEntity trainingrecord = trainingrecordService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(trainingrecord,deSens);
        return R.ok().put("data", trainingrecord);
    }

    private void fillPlanInfo(TrainingrecordEntity trainingrecord) {
        if (trainingrecord == null || trainingrecord.getPlanid() == null) {
            return;
        }
        TrainingplanEntity plan = trainingplanService.getById(trainingrecord.getPlanid());
        if (plan != null) {
            if (trainingrecord.getPlanname() == null) {
                trainingrecord.setPlanname(plan.getPlanname());
            }
            if (trainingrecord.getUserid() == null) {
                trainingrecord.setUserid(plan.getUserid());
            }
            if (trainingrecord.getUseraccount() == null) {
                trainingrecord.setUseraccount(plan.getUseraccount());
            }
            if (trainingrecord.getUsername() == null) {
                trainingrecord.setUsername(plan.getUsername());
            }
            if (trainingrecord.getCoachid() == null) {
                trainingrecord.setCoachid(plan.getCoachid());
            }
            if (trainingrecord.getCoachaccount() == null) {
                trainingrecord.setCoachaccount(plan.getCoachaccount());
            }
            if (trainingrecord.getCoachname() == null) {
                trainingrecord.setCoachname(plan.getCoachname());
            }
        }
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody TrainingrecordEntity trainingrecord, HttpServletRequest request){
        trainingrecord.setId(null);
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("coach".equals(tableName) && trainingrecord.getCoachid() == null) {
            trainingrecord.setCoachid((Long)request.getSession().getAttribute("userId"));
        }
        if ("user".equals(tableName)) {
            return R.error("仅教练可填写训练记录");
        }
        fillPlanInfo(trainingrecord);
        trainingrecordService.save(trainingrecord);
        operationLogRecorder.record("trainingrecord", "训练记录", "新增", trainingrecord, request);
        return R.ok().put("data",trainingrecord.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody TrainingrecordEntity trainingrecord, HttpServletRequest request){
        trainingrecord.setId(null);
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("coach".equals(tableName) && trainingrecord.getCoachid() == null) {
            trainingrecord.setCoachid((Long)request.getSession().getAttribute("userId"));
        }
        if ("user".equals(tableName)) {
            return R.error("仅教练可填写训练记录");
        }
        fillPlanInfo(trainingrecord);
        trainingrecordService.save(trainingrecord);
        operationLogRecorder.record("trainingrecord", "训练记录", "新增", trainingrecord, request);
        return R.ok().put("data",trainingrecord.getId());
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody TrainingrecordEntity trainingrecord, HttpServletRequest request){
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("user".equals(tableName)) {
            return R.error("训练记录由教练维护，用户不可修改");
        }
        trainingrecordService.updateById(trainingrecord);
        operationLogRecorder.record("trainingrecord", "训练记录", "修改", trainingrecord, request);
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
            return R.error("训练记录由教练维护，用户不可删除");
        }
        trainingrecordService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("trainingrecord", "训练记录", "删除", Arrays.asList(ids), request);
        return R.ok();
    }
}
