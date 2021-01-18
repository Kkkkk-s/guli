package com.atguigu.eduservice.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EduTeacherVo {
    @ApiModelProperty(value = "讲师姓名")
    private String name;
    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;
    @ApiModelProperty(value = "查询开始时间",example = "2020-12-25 10:10:10")
    private String begin;

    @ApiModelProperty(value = "查询结束时间",example = "2020-1-25 10:10:10")
    private String end;
}
