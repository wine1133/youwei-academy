package com.example.service;
import com.example.entity.Chapter;
import com.example.mapper.ChapterMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ChapterService {
    @Resource private ChapterMapper chapterMapper;
    public void add(Chapter c){if(c.getSortNo()==null)c.setSortNo(chapterMapper.countByBookId(c.getBookId())+1);chapterMapper.insert(c);}
    public void updateById(Chapter c){chapterMapper.updateById(c);}
    public void deleteById(Integer id){chapterMapper.deleteById(id);}
    public Chapter selectById(Integer id){return chapterMapper.selectById(id);}
    public List<Chapter> selectByBookId(Integer bookId){return chapterMapper.selectByBookId(bookId);}
}
