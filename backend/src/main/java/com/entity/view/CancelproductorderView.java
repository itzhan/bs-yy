package com.entity.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.entity.CancelproductorderEntity;
import java.math.BigDecimal;
import java.io.Serializable;
import java.io.Serial;
import org.springframework.beans.BeanUtils;

/**
 * 取消商品订单
 *  后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("cancelproductorder")
public class CancelproductorderView extends CancelproductorderEntity implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;


	public CancelproductorderView(){

	}
 
	public CancelproductorderView(CancelproductorderEntity cancelproductorderEntity){
        BeanUtils.copyProperties(this, cancelproductorderEntity);
    }


}
