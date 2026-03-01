package com.entity.view;

import com.baomidou.mybatisplus.annotation.TableName;
import com.entity.TrainingplanEntity;
import java.io.Serializable;
import java.io.Serial;
import org.springframework.beans.BeanUtils;

/**
 * 训练计划
 * 后端返回视图实体辅助类
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("trainingplan")
public class TrainingplanView extends TrainingplanEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public TrainingplanView(){
    }

    public TrainingplanView(TrainingplanEntity trainingplanEntity){
        BeanUtils.copyProperties(this, trainingplanEntity);
    }
}
