package com.example.service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.*;
@Service
public class DashboardService {
    @Resource private JdbcTemplate jdbc;
    public Map<String,Object> stats(){
        Map<String,Object> m=new LinkedHashMap<>();
        m.put("bookCount",jdbc.queryForObject("select count(*) from book",Integer.class));
        m.put("userCount",jdbc.queryForObject("select count(*) from user",Integer.class));
        m.put("teacherCount",jdbc.queryForObject("select count(*) from teacher",Integer.class));
        m.put("borrowCount",jdbc.queryForObject("select count(*) from borrow",Integer.class));
        m.put("activeBorrow",jdbc.queryForObject("select count(*) from borrow where status='BORROWED'",Integer.class));
        m.put("articleCount",jdbc.queryForObject("select count(*) from article",Integer.class));
        m.put("homeworkCount",jdbc.queryForObject("select count(*) from homework",Integer.class));
        m.put("submitCount",jdbc.queryForObject("select count(*) from submit",Integer.class));
        m.put("categories",jdbc.queryForList("select category name,count(*) value from book group by category order by value desc"));
        m.put("borrowTrend",jdbc.queryForList("select date_format(borrow_time,'%m-%d') day,count(*) value from borrow where borrow_time>=date_sub(now(),interval 7 day) group by day order by day"));
        m.put("recentBooks",jdbc.queryForList("select id,name,author,cover,category,score,stock from book order by id desc limit 5"));
        return m;
    }
}
