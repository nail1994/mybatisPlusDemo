package com.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dwb
 * @create 2020-12-02 10:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User
{
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer age;

    private String email;

    @Version
    private Integer version;

}
