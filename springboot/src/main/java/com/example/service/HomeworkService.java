package com.example.service;
import com.example.entity.Homework;
import com.example.exception.CustomException;
import com.example.mapper.HomeworkMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class HomeworkService {
    @Resource private HomeworkMapper homeworkMapper;
    @Resource private MessageService messageService;
    @Transactional
    public void add(Homework h){if(h.getTitle()==null||h.getTitle().isBlank())throw new CustomException("作业标题不能为空");homeworkMapper.insert(h);messageService.sendToRole("USER","新的学习任务", "老师发布了新的学习任务：" + h.getTitle(), "HOMEWORK", h.getId());}
    public void updateById(Homework h){homeworkMapper.updateById(h);}
    public void deleteById(Integer id){homeworkMapper.deleteById(id);}
    public Homework selectById(Integer id){return homeworkMapper.selectById(id);}
    public List<Homework> selectAll(Homework h){return homeworkMapper.selectAll(h);}
    public PageInfo<Homework> selectPage(Homework h,Integer p,Integer s){PageHelper.startPage(p,s);return PageInfo.of(homeworkMapper.selectAll(h));}
    public int countAll(){return homeworkMapper.countAll();}
}

