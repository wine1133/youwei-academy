package com.example.service;
import com.example.entity.Borrow;
import com.example.exception.CustomException;
import com.example.mapper.BookMapper;
import com.example.mapper.BorrowMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class BorrowService {
    @Resource private BorrowMapper borrowMapper;
    @Resource private BookMapper bookMapper;
    @Resource private MessageService messageService;
    @Transactional
    public void borrow(Borrow borrow){
        if(borrow.getBookId()==null||borrow.getUserId()==null) throw new CustomException("借阅信息不完整");
        if(borrowMapper.countActiveByUserAndBook(borrow.getUserId(),borrow.getBookId())>0) throw new CustomException("该书已借阅，尚未归还");
        if(borrowMapper.countActiveByUserId(borrow.getUserId())>=5) throw new CustomException("最多同时借阅 5 本图书");
        if(bookMapper.decreaseStock(borrow.getBookId())==0) throw new CustomException("该书暂无可借库存");
        borrow.setDueTime(LocalDateTime.now().plusDays(30));
        borrowMapper.insert(borrow);
        var book = bookMapper.selectById(borrow.getBookId());
        messageService.sendToUser(borrow.getUserId(), "USER", "借阅成功", "你已借阅《" + (book == null ? "图书" : book.getName()) + "》，请按时归还。", "BORROW", borrow.getBookId());
    }
    @Transactional
    public void returnBook(Integer id){
        Borrow b=borrowMapper.selectById(id);
        if(b==null) throw new CustomException("借阅记录不存在");
        if(!"BORROWED".equals(b.getStatus())) throw new CustomException("该记录已归还");
        borrowMapper.returnBook(b); bookMapper.increaseStock(b.getBookId());
    }
    public List<Borrow> selectAll(Borrow b){return borrowMapper.selectAll(b);}
    public PageInfo<Borrow> selectPage(Borrow b,Integer p,Integer s){PageHelper.startPage(p,s);return PageInfo.of(borrowMapper.selectAll(b));}
    public int countActive(){return borrowMapper.countActive();}
}



