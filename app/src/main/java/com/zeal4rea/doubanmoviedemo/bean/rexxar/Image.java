package com.zeal4rea.doubanmoviedemo.bean.rexxar;

import java.io.Serializable;

public class Image implements Serializable {
    public ImageInfo large;
    public ImageInfo normal;
    public ImageInfo raw;
    public ImageInfo small;

    public static class ImageInfo implements Serializable {
        public String height;
        public String width;
        public String size;
        public String url;
    }
}
