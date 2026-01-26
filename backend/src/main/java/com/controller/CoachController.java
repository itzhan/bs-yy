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

import com.entity.CoachEntity;
import com.entity.view.CoachView;

import com.service.CoachService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 教练
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/coach")
public class CoachController {
    @Resource
    private CoachService coachService;
    @Resource
    private TokenService tokenService;
    @Resource
    private OperationLogRecorder operationLogRecorder;
    /**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		CoachEntity u = coachService.getOne(new QueryWrapper<CoachEntity>().eq("coachaccount", username));
		if(u==null || !u.getCoachpassword().equals(password)) {
			return R.error("账号或密码不正确");
		}
		String token = tokenService.generateToken(u.getId(), username,"coach", "教练");
		return R.ok().put("token", token);
	}
	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody CoachEntity coach){
    	//ValidatorUtils.validateEntity(coach);
    	CoachEntity u = coachService.getOne(new QueryWrapper<CoachEntity>().eq("coachaccount", coach.getCoachaccount()));
		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = IdUtil.getSnowflakeNextId();
		coach.setId(uId);
        coachService.save(coach);
        return R.ok();
    }

	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        CoachEntity u = coachService.getById(id);
        return R.ok().put("data", u);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	CoachEntity u = coachService.getOne(new QueryWrapper<CoachEntity>().eq("coachaccount", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setCoachpassword("123456");
        coachService.updateById(u);
        return R.ok("密码已重置为：123456");
    }

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CoachEntity coach,
            HttpServletRequest request){
        QueryWrapper<CoachEntity> ew = new QueryWrapper<CoachEntity>();
        PageUtils page = coachService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, coach), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CoachEntity coach,
            HttpServletRequest request){
        QueryWrapper<CoachEntity> ew = new QueryWrapper<CoachEntity>();
        PageUtils page = coachService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, coach), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(CoachEntity coach){
        QueryWrapper<CoachEntity> ew = new QueryWrapper<CoachEntity>();
        ew.allEq(MPUtil.allEQMapPre(coach, "coach"));
        return R.ok().put("data", coachService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(CoachEntity coach){
        QueryWrapper<CoachEntity> ew = new QueryWrapper<CoachEntity>();
        ew.allEq(MPUtil.allEQMapPre(coach, "coach"));
        CoachView coachView = coachService.selectView(ew);
        return R.ok("查询教练成功").put("data", coachView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CoachEntity coach = coachService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(coach,deSens);
        return R.ok().put("data", coach);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CoachEntity coach = coachService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(coach,deSens);
        return R.ok().put("data", coach);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody CoachEntity coach, HttpServletRequest request){
        //ValidatorUtils.validateEntity(coach);
        coach.setId(null);
        // 账号唯一校验
        if (coachService.count(new QueryWrapper<CoachEntity>().eq("coachaccount",coach.getCoachaccount())) > 0){
            return R.error("账号已存在");
        }
        // 工号唯一校验
        if (coachService.count(new QueryWrapper<CoachEntity>().eq("jobno",coach.getJobno())) > 0){
            return R.error("工号已存在");
        }
        coachService.save(coach);
        operationLogRecorder.record("coach", "教练", "新增", coach, request);
        return R.ok().put("data",coach.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody CoachEntity coach, HttpServletRequest request){
        //ValidatorUtils.validateEntity(coach);
        coach.setId(null);
        // 账号唯一校验
        if (coachService.count(new QueryWrapper<CoachEntity>().eq("coachaccount",coach.getCoachaccount())) > 0){
            return R.error("账号已存在");
        }
        // 工号唯一校验
        if (coachService.count(new QueryWrapper<CoachEntity>().eq("jobno",coach.getJobno())) > 0){
            return R.error("工号已存在");
        }
        coachService.save(coach);
        operationLogRecorder.record("coach", "教练", "新增", coach, request);
        return R.ok().put("data",coach.getId());
    }

    /**
    * 获取用户密保
    */
    @IgnoreAuth
    @RequestMapping("/security")
    public R security(@RequestParam String username){
        CoachEntity coach = coachService.getOne(new QueryWrapper<CoachEntity>().eq("coachaccount", username));
        return R.ok().put("data", coach);
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody CoachEntity coach, HttpServletRequest request){
        //ValidatorUtils.validateEntity(coach);
        //全部更新
        coachService.updateById(coach);
        operationLogRecorder.record("coach", "教练", "修改", coach, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        coachService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("coach", "教练", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, CoachEntity coach, HttpServletRequest request, String pre){
        QueryWrapper<CoachEntity> ew = new QueryWrapper<CoachEntity>();
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
        PageUtils page = coachService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, coach), params), params));
        return R.ok().put("data", page);
    }





}
