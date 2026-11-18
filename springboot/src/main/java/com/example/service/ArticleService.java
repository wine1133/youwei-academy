package com.example.service;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ArticleService {
    @Resource private ArticleMapper articleMapper;
    @Resource private PraiseMapper praiseMapper;
    @Resource private CollectMapper collectMapper;
    @Resource private CommentsMapper commentsMapper;
    public void add(Article a){if(a.getTitle()==null||a.getTitle().isBlank())throw new CustomException("标题不能为空");if(a.getSummary()==null||a.getSummary().isBlank())a.setSummary("分享阅读与思考，让知识真正流动起来。");if(a.getCover()==null||a.getCover().isBlank())a.setCover("/images/covers/article-01.svg");if(a.getStatus()==null)a.setStatus("PUBLISHED");articleMapper.insert(a);}
    public void updateById(Article a){articleMapper.updateById(a);}
    public void deleteById(Integer id){articleMapper.deleteById(id);}
    public Article detail(Integer id){articleMapper.increaseViews(id);return articleMapper.selectById(id);}
    public List<Article> selectAll(Article a){return articleMapper.selectAll(a);}
    public PageInfo<Article> selectPage(Article a,Integer p,Integer s){PageHelper.startPage(p,s);return PageInfo.of(articleMapper.selectAll(a));}
    public boolean toggleLike(Integer articleId,Integer userId){Praise old=praiseMapper.selectOne(articleId,userId);if(old!=null){praiseMapper.deleteOne(articleId,userId);articleMapper.changeLikes(articleId,-1);return false;}Praise p=new Praise();p.setArticleId(articleId);p.setUserId(userId);praiseMapper.insert(p);articleMapper.changeLikes(articleId,1);return true;}
    public boolean liked(Integer articleId,Integer userId){return praiseMapper.selectOne(articleId,userId)!=null;}
    public boolean toggleCollect(Integer articleId,Integer userId){Collect old=collectMapper.selectOne(articleId,userId);if(old!=null){collectMapper.deleteOne(articleId,userId);articleMapper.changeCollects(articleId,-1);return false;}Collect c=new Collect();c.setArticleId(articleId);c.setUserId(userId);collectMapper.insert(c);articleMapper.changeCollects(articleId,1);return true;}
    public boolean collected(Integer articleId,Integer userId){return collectMapper.selectOne(articleId,userId)!=null;}
    public List<Article> myCollects(Integer userId){return collectMapper.selectArticlesByUser(userId);}
    public void addComment(Comments c){if(c.getContent()==null||c.getContent().isBlank())throw new CustomException("评论不能为空");commentsMapper.insert(c);}
    public List<Comments> comments(Integer articleId){return commentsMapper.selectByArticle(articleId);}
    public void deleteComment(Integer id){commentsMapper.deleteById(id);}
    public List<Comments> allComments(){return commentsMapper.selectAll();}
    public int countArticles(){return articleMapper.countAll();}
    public int countComments(){return commentsMapper.countAll();}
}
