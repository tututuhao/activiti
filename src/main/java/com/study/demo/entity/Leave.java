package com.study.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 请假流程
 * </p>
 *
 * @author tuhao
 * @since 2024-06-25
 */
@Getter
@Setter
@TableName("EDU_LEAVE")
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 请假人名称
     */
    private String name;

    /**
     * 请假类型
     */
    private String leaveType;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 是否上午
     */
    private Integer isam;

    /**
     * 请假理由
     */
    private String leaveReason;

    /**
     * 请假天数
     */
    private BigDecimal leaveDay;
}
