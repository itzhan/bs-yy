package com.entity.view;

import com.baomidou.mybatisplus.annotation.TableName;
import com.entity.CoachreviewEntity;
import java.io.Serializable;
import java.io.Serial;
import org.springframework.beans.BeanUtils;

/**
 * 教练评价
 * 后端返回视图实体辅助类
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("coachreview")
public class CoachreviewView extends CoachreviewEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public CoachreviewView(){
    }

    public CoachreviewView(CoachreviewEntity coachreviewEntity){
        BeanUtils.copyProperties(this, coachreviewEntity);
    }
}
