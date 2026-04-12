package com.entity.view;

import com.baomidou.mybatisplus.annotation.TableName;
import com.entity.PaymentsettingEntity;
import java.io.Serializable;
import java.io.Serial;
import org.springframework.beans.BeanUtils;

/**
 * 支付设置
 * @author ds
 */
@TableName("paymentsetting")
public class PaymentsettingView extends PaymentsettingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public PaymentsettingView() {}

    public PaymentsettingView(PaymentsettingEntity entity) {
        BeanUtils.copyProperties(this, entity);
    }
}
