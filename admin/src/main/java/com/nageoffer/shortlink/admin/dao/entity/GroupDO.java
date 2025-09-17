package com.nageoffer.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @description t_group
 * @author wjz
 * @date 2025-09-17
 */
@TableName("t_group")
@Data
@Builder
@AllArgsConstructor
public class GroupDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * ID
     */
    private Long id;

    /**
     * 分组ID 不同用户的分组ID可以一致
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建分组的用户名
     */
    private String username;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标识 0：未删除 1：已删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 分组排序
     */
    private Integer sortOrder;

    public GroupDO() {}
}