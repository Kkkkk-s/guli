package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.excl.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GlobalExceptionHanler;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    public EduSubjectService subjectService;
    public SubjectExcelListener( ) { }
    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }


    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData ==null){
            throw new GuliException(20001,"文件数据为空");
        }
        //添加一级分类
        EduSubject eduSubject = this.oneSubject(subjectService, subjectData.getOneSubjectName());
        if (eduSubject ==null){
            eduSubject = new EduSubject();
            eduSubject.setParentId("0");
            eduSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(eduSubject);
        }
        String pid = eduSubject.getId();

        //添加二级分类
        //是否重复
        EduSubject subject = this.twoSubject(subjectService, subjectData.getTwoSubjectName(), pid);
        if (subject ==null){
            subject= new EduSubject();
            subject.setParentId(pid);
            subject.setTitle(subjectData.getTwoSubjectName());
            subjectService.save(subject);
        }
    }

    //一级分类不能重复
    private EduSubject oneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",0);
        EduSubject one = subjectService.getOne(wrapper);
        return one;
    }

    private EduSubject twoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject two = subjectService.getOne(wrapper);
        return two;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
