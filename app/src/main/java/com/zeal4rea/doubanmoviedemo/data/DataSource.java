package com.zeal4rea.doubanmoviedemo.data;

import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Subject4J;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWorkWrapper;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Photo;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.SubjectCelebrities;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public interface DataSource {
    Observable<CommonResult<Void, com.zeal4rea.doubanmoviedemo.bean.api.Subject>> apiGetSubjects(SubjectsType type, int start, int count);
    Observable<CommonResult<Void, com.zeal4rea.doubanmoviedemo.bean.api.Subject>> apiQuerySubjects(Map<String, String> queryMap);
    Observable<com.zeal4rea.doubanmoviedemo.bean.api.Subject> apiGetSubjectDetail(String subjectId);
    Observable<com.zeal4rea.doubanmoviedemo.bean.api.Celebrity> apiGetCelebrity(String celebrityId);

    Observable<Subject4J> htmlGetSubjectDetail(String subjectId);
    Observable<CommonResult<Void, PhotoTemp>> htmlGetPhotos(String subjectId, int start, int type);
    Observable<List<Comment4J>> htmlGetComments(String subjectId, int start, String sort);
    Observable<List<Review4J>> htmlGetReviews(String subjectId, int start, int count);

    Observable<com.zeal4rea.doubanmoviedemo.bean.rexxar.Subject> rexxarGetSubjectInfo(String id);
    Observable<SubjectCelebrities> rexxarGetSubjectCelebrities(String id);
    Observable<CommonResult<Void, Comment>> rexxarGetSubjectComments(String id, String sort, int start, int count);
    Observable<com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity> rexxarGetCelebrityDetail(String id);
    Observable<CommonResult<Void, CelebrityWorkWrapper>> rexxarGetCelebrityWorks(String id, String sort, int start, int count);
    Observable<CommonResult<Void, Photo>> rexxarGetCelebrityPhotos(String id, int start, int count);
}
