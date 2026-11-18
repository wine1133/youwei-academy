package com.example.service;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserService {
    @Resource private UserMapper userMapper;
    public void add(User user){
        if(ObjectUtil.isEmpty(user.getUsername())) throw new CustomException("账号不能为空");
        if(userMapper.selectByUsername(user.getUsername())!=null) throw new CustomException("账号已存在");
        if(ObjectUtil.isEmpty(user.getPassword())) user.setPassword("123456");
        if(ObjectUtil.isEmpty(user.getName())) user.setName(user.getUsername());
        user.setRole("USER"); userMapper.insert(user);
    }
    public void updateById(User user){
        User db=userMapper.selectById(user.getId()); if(db==null) throw new CustomException("用户不存在");
        User same=userMapper.selectByUsername(user.getUsername()); if(same!=null&&!same.getId().equals(user.getId())) throw new CustomException("账号已存在");
        if(ObjectUtil.isEmpty(user.getPassword())) user.setPassword(db.getPassword());
        user.setRole("USER"); userMapper.updateById(user);
    }
    public void deleteById(Integer id){userMapper.deleteById(id);}
    public User selectById(Integer id){return userMapper.selectById(id);}
    public List<User> selectAll(User user){return userMapper.selectAll(user);}
    public PageInfo<User> selectPage(User user,Integer p,Integer s){PageHelper.startPage(p,s);return PageInfo.of(userMapper.selectAll(user));}
    public Account login(Account a){User u=userMapper.selectByUsername(a.getUsername());if(u==null)throw new CustomException("用户不存在");if(!a.getPassword().equals(u.getPassword()))throw new CustomException("账号或密码错误");return u;}
    public void register(Account a){User u=new User();u.setUsername(a.getUsername());u.setPassword(a.getPassword());add(u);}
    public void updatePassword(Account a){User u=userMapper.selectByUsername(a.getUsername());if(u==null)throw new CustomException("用户不存在");if(!a.getPassword().equals(u.getPassword()))throw new CustomException("原密码错误");if(ObjectUtil.isEmpty(a.getNewPassword()))throw new CustomException("新密码不能为空");u.setPassword(a.getNewPassword());userMapper.updateById(u);}
}
