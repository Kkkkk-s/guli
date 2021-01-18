package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.eduservice.vo.EduTeacherVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Kkkk
 * @since 2020-12-24
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "查询所有僵尸")
    @GetMapping("findAll")
    public R findAll() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("item", list);
    }

    //2 逻辑删除讲师的方法
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师ID", required = true)
                           @PathVariable String id) {
        boolean flag = eduTeacherService.removeById(id);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("page/{current}/{limit}")
    public R page(@PathVariable Long current,
                  @PathVariable Long limit) {
        Page<EduTeacher> page = new Page<>(current, limit);


        eduTeacherService.page(page,null);

        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }

    @PostMapping("pagelist/{current}/{limit}")
    public R pageList(@PathVariable Long current,
                      @PathVariable Long limit,
                      @RequestBody(required = false) EduTeacherVo eduTeacherVo){
        Page<EduTeacher> page = new Page<>(current,limit);

        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询
        String name = eduTeacherVo.getName();
        Integer level = eduTeacherVo.getLevel();
        String begin = eduTeacherVo.getBegin();
        String end = eduTeacherVo.getEnd();
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
       if (!StringUtils.isEmpty(level)){
           wrapper.eq("level",level);
       }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.eq("gmt_modified",end);
        }

        wrapper.orderByDesc("gmt_create");

        eduTeacherService.page(page,wrapper);

        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        return R.ok().data("total",total).data("rows",records);

    }

    //讲师添加
    @PostMapping("add")
    public R teaAdd(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else {
            return R.error();
        }
    }
    //根据讲师id进行查询
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    //讲师修改功能
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }



}

