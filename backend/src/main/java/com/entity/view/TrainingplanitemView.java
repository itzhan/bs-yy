package com.entity.view;

import com.baomidou.mybatisplus.annotation.TableName;
import com.entity.TrainingplanitemEntity;
import java.io.Serializable;
import java.io.Serial;
import org.springframework.beans.BeanUtils;

/**
 * 训练计划明细
 * 后端返回视图实体辅助类
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("trainingplanitem")
public class TrainingplanitemView extends TrainingplanitemEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public TrainingplanitemView(){
    }

    public TrainingplanitemView(TrainingplanitemEntity trainingplanitemEntity){
        BeanUtils.copyProperties(this, trainingplanitemEntity);
    }
}
