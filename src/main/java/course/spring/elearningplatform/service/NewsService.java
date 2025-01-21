package course.spring.elearningplatform.service;

import course.spring.elearningplatform.entity.News;

import java.util.List;

public interface NewsService {

    List<News> getAllNews();
    void deleteNews(Long id) ;
    News getNewsById(Long id);
}
