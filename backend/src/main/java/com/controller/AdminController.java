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

import com.entity.AdminEntity;
import com.entity.view.AdminView;

import com.service.AdminService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 管理员
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
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
		AdminEntity u = adminService.getOne(new QueryWrapper<AdminEntity>().eq("adminaccount", username));
		if(u==null || !u.getAdminpassword().equals(password)) {
			return R.error("账号或密码不正确");
		}
		String token = tokenService.generateToken(u.getId(), username,"admin", "管理员");
		return R.ok().put("token", token);
	}
	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody AdminEntity admin){
    	//ValidatorUtils.validateEntity(admin);
    	AdminEntity u = adminService.getOne(new QueryWrapper<AdminEntity>().eq("adminaccount", admin.getAdminaccount()));
		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = IdUtil.getSnowflakeNextId();
		admin.setId(uId);
        adminService.save(admin);
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
        AdminEntity u = adminService.getById(id);
        return R.ok().put("data", u);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	AdminEntity u = adminService.getOne(new QueryWrapper<AdminEntity>().eq("adminaccount", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setAdminpassword("123456");
        adminService.updateById(u);
        return R.ok("密码已重置为：123456");
    }

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, AdminEntity admin,
            HttpServletRequest request){
        QueryWrapper<AdminEntity> ew = new QueryWrapper<AdminEntity>();
        PageUtils page = adminService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, admin), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, AdminEntity admin,
            HttpServletRequest request){
        QueryWrapper<AdminEntity> ew = new QueryWrapper<AdminEntity>();
        PageUtils page = adminService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, admin), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(AdminEntity admin){
        QueryWrapper<AdminEntity> ew = new QueryWrapper<AdminEntity>();
        ew.allEq(MPUtil.allEQMapPre(admin, "admin"));
        return R.ok().put("data", adminService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(AdminEntity admin){
        QueryWrapper<AdminEntity> ew = new QueryWrapper<AdminEntity>();
        ew.allEq(MPUtil.allEQMapPre(admin, "admin"));
        AdminView adminView = adminService.selectView(ew);
        return R.ok("查询管理员成功").put("data", adminView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        AdminEntity admin = adminService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(admin,deSens);
        return R.ok().put("data", admin);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        AdminEntity admin = adminService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(admin,deSens);
        return R.ok().put("data", admin);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody AdminEntity admin, HttpServletRequest request){
        //ValidatorUtils.validateEntity(admin);
        admin.setId(null);
        // 账号唯一校验
        if (adminService.count(new QueryWrapper<AdminEntity>().eq("adminaccount",admin.getAdminaccount())) > 0){
            return R.error("账号已存在");
        }
        adminService.save(admin);
        operationLogRecorder.record("admin", "管理员", "新增", admin, request);
        return R.ok().put("data",admin.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody AdminEntity admin, HttpServletRequest request){
        //ValidatorUtils.validateEntity(admin);
        admin.setId(null);
        // 账号唯一校验
        if (adminService.count(new QueryWrapper<AdminEntity>().eq("adminaccount",admin.getAdminaccount())) > 0){
            return R.error("账号已存在");
        }
        adminService.save(admin);
        operationLogRecorder.record("admin", "管理员", "新增", admin, request);
        return R.ok().put("data",admin.getId());
    }

    /**
    * 获取用户密保
    */
    @IgnoreAuth
    @RequestMapping("/security")
    public R security(@RequestParam String username){
        AdminEntity admin = adminService.getOne(new QueryWrapper<AdminEntity>().eq("adminaccount", username));
        return R.ok().put("data", admin);
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody AdminEntity admin, HttpServletRequest request){
        //ValidatorUtils.validateEntity(admin);
        //全部更新
        adminService.updateById(admin);
        operationLogRecorder.record("admin", "管理员", "修改", admin, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        adminService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("admin", "管理员", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, AdminEntity admin, HttpServletRequest request, String pre){
        QueryWrapper<AdminEntity> ew = new QueryWrapper<AdminEntity>();
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
        PageUtils page = adminService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, admin), params), params));
        return R.ok().put("data", page);
    }





}
