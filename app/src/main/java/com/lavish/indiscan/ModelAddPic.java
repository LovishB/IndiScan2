package com.lavish.indiscan;

import android.graphics.Bitmap;

public class ModelAddPic {

    String ProjectId;
    String PicId;
    Bitmap mBitmap;

    public ModelAddPic(String projectId, String picId, Bitmap bitmap) {
        ProjectId = projectId;
        PicId = picId;
        mBitmap = bitmap;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public String getPicId() {
        return PicId;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
