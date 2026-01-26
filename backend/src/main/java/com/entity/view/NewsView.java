package com.entity.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.entity.NewsEntity;
import java.math.BigDecimal;
import java.io.Serializable;
import java.io.Serial;
import org.springframework.beans.BeanUtils;

/**
 * 公告资讯
 *  后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("news")
public class NewsView extends NewsEntity implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;


	public NewsView(){

	}
 
	public NewsView(NewsEntity newsEntity){
        BeanUtils.copyProperties(this, newsEntity);
    }


}
