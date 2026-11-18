package com.example.service;
import com.example.entity.Space;
import com.example.mapper.SpaceMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class SpaceService {
    @Resource private SpaceMapper spaceMapper;
    public List<Space> selectAll(){return spaceMapper.selectAll();}
    public void add(Space s){spaceMapper.insert(s);}
    public void update(Space s){spaceMapper.updateById(s);}
    public void delete(Integer id){spaceMapper.deleteById(id);}
}
