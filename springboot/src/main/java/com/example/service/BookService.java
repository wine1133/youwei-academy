package com.example.service;
import com.example.entity.Book;
import com.example.exception.CustomException;
import com.example.mapper.BookMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
@Service
public class BookService {
    @Resource private BookMapper bookMapper;
    public void add(Book b){if(b.getName()==null||b.getName().isBlank())throw new CustomException("书名不能为空");if(b.getTotal()==null)b.setTotal(1);if(b.getStock()==null)b.setStock(b.getTotal());if(b.getScore()==null)b.setScore(new BigDecimal("5.0"));if(b.getStatus()==null)b.setStatus("AVAILABLE");bookMapper.insert(b);}
    public void updateById(Book b){bookMapper.updateById(b);}
    public void deleteById(Integer id){bookMapper.deleteById(id);}
    public Book selectById(Integer id){return bookMapper.selectById(id);}
    public List<Book> selectAll(Book b){return bookMapper.selectAll(b);}
    public PageInfo<Book> selectPage(Book b,Integer p,Integer s){PageHelper.startPage(p,s);return PageInfo.of(bookMapper.selectAll(b));}
}
