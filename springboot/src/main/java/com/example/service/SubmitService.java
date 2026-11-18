package com.example.service;
import com.example.entity.Submit;
import com.example.entity.Homework;
import com.example.exception.CustomException;
import com.example.mapper.SubmitMapper;
import com.example.mapper.HomeworkMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class SubmitService {
    @Resource private SubmitMapper submitMapper;
    @Resource private HomeworkMapper homeworkMapper;
    @Resource private MessageService messageService;
    @Transactional
    public void add(Submit s){if(submitMapper.countByHomeworkAndUser(s.getHomeworkId(),s.getUserId())>0)throw new CustomException("该作业已提交，请勿重复提交");submitMapper.insert(s);Homework h=homeworkMapper.selectById(s.getHomeworkId());if(h!=null&&h.getTeacherId()!=null){messageService.sendToUser(h.getTeacherId(),"TEACHER","收到新的作业提交","学生提交了《"+h.getTitle()+"》，请及时批改。","SUBMIT",h.getId());}}
    @Transactional
    public void grade(Submit s){Submit db=submitMapper.selectById(s.getId());submitMapper.grade(s);if(db!=null){Homework h=homeworkMapper.selectById(db.getHomeworkId());String title=h==null?"作业":h.getTitle();messageService.sendToUser(db.getUserId(),"USER","作业已批改","你的《"+title+"》已批改，得分："+s.getScore(),"GRADE",db.getHomeworkId());}}
    public void deleteById(Integer id){submitMapper.deleteById(id);}
    public List<Submit> selectAll(Submit s){return submitMapper.selectAll(s);}
    public PageInfo<Submit> selectPage(Submit s,Integer p,Integer size){PageHelper.startPage(p,size);return PageInfo.of(submitMapper.selectAll(s));}
    public int countAll(){return submitMapper.countAll();}
}


