package com.example.service;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Teacher;
import com.example.exception.CustomException;
import com.example.mapper.TeacherMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TeacherService {
    @Resource private TeacherMapper teacherMapper;
    public void add(Teacher t){
        if(ObjectUtil.isEmpty(t.getUsername())) throw new CustomException("账号不能为空");
        if(teacherMapper.selectByUsername(t.getUsername())!=null) throw new CustomException("账号已存在");
        if(ObjectUtil.isEmpty(t.getPassword())) t.setPassword("123456");
        if(ObjectUtil.isEmpty(t.getName())) t.setName(t.getUsername());
        t.setRole("TEACHER"); teacherMapper.insert(t);
    }
    public void updateById(Teacher t){Teacher db=teacherMapper.selectById(t.getId());if(db==null)throw new CustomException("教师不存在");if(ObjectUtil.isEmpty(t.getPassword()))t.setPassword(db.getPassword());t.setRole("TEACHER");teacherMapper.updateById(t);}
    public void deleteById(Integer id){teacherMapper.deleteById(id);}
    public Teacher selectById(Integer id){return teacherMapper.selectById(id);}
    public List<Teacher> selectAll(Teacher t){return teacherMapper.selectAll(t);}
    public PageInfo<Teacher> selectPage(Teacher t,Integer p,Integer s){PageHelper.startPage(p,s);return PageInfo.of(teacherMapper.selectAll(t));}
    public Account login(Account a){Teacher t=teacherMapper.selectByUsername(a.getUsername());if(t==null)throw new CustomException("教师不存在");if(!a.getPassword().equals(t.getPassword()))throw new CustomException("账号或密码错误");return t;}
    public void register(Account a){Teacher t=new Teacher();t.setUsername(a.getUsername());t.setPassword(a.getPassword());add(t);}
    public void updatePassword(Account a){Teacher t=teacherMapper.selectByUsername(a.getUsername());if(t==null)throw new CustomException("教师不存在");if(!a.getPassword().equals(t.getPassword()))throw new CustomException("原密码错误");if(ObjectUtil.isEmpty(a.getNewPassword()))throw new CustomException("新密码不能为空");t.setPassword(a.getNewPassword());teacherMapper.updateById(t);}
}
