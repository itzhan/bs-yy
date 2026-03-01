package com.entity.view;

import com.baomidou.mybatisplus.annotation.TableName;
import com.entity.CoachmemberEntity;
import java.io.Serializable;
import java.io.Serial;
import org.springframework.beans.BeanUtils;

/**
 * 教练-会员绑定
 * 后端返回视图实体辅助类
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("coachmember")
public class CoachmemberView extends CoachmemberEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public CoachmemberView(){
    }

    public CoachmemberView(CoachmemberEntity coachmemberEntity){
        BeanUtils.copyProperties(this, coachmemberEntity);
    }
}
