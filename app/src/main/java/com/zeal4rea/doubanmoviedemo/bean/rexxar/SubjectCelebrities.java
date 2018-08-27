package com.zeal4rea.doubanmoviedemo.bean.rexxar;

import java.io.Serializable;

public class SubjectCelebrities implements Serializable {
    public CelebritiesWrapper[] credits;

    public static class CelebritiesWrapper implements Serializable {
        public Celebrity[] celebrities;
        public String title;
    }
}
