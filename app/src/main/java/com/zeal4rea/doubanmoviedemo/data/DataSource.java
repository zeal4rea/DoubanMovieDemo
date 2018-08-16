package com.zeal4rea.doubanmoviedemo.data;

import com.zeal4rea.doubanmoviedemo.bean.api.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Subject4J;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public interface DataSource {
    Observable<CommonResult<Void, Subject>> apiGetSubjects(SubjectsType type);
    Observable<CommonResult<Void, Subject>> apiQuerySubjects(Map<String, String> queryMap);
    Observable<Subject> apiGetSubjectDetail(String subjectId);
    Observable<Celebrity> apiGetCelebrity(String celebrityId);

    Observable<Subject4J> htmlGetSubjectDetail(String subjectId);
    Observable<CommonResult<Void, PhotoTemp>> htmlGetPhotos(String subjectId, int start);
    Observable<List<Comment4J>> htmlGetComments(String subjectId, int start, String sort);
    Observable<List<Review4J>> htmlGetReviews(String subjectId, int start, int count);
}
