package com.entity.view;

import com.baomidou.mybatisplus.annotation.TableName;
import com.entity.TrainingrecordEntity;
import java.io.Serializable;
import java.io.Serial;
import org.springframework.beans.BeanUtils;

/**
 * 训练记录
 * 后端返回视图实体辅助类
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("trainingrecord")
public class TrainingrecordView extends TrainingrecordEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public TrainingrecordView(){
    }

    public TrainingrecordView(TrainingrecordEntity trainingrecordEntity){
        BeanUtils.copyProperties(this, trainingrecordEntity);
    }
}
