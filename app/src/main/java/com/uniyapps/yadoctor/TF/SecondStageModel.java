package com.uniyapps.yadoctor.TF;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class SecondStageModel extends ImageClassifierGeneral {

    public SecondStageModel(Activity activity) {
        super(activity);
    }

    @Override
    protected String getModelPath() {
        return "second.lite";
    }

    @Override
    protected List<String> getLabelPath() {
        List<String> labelList = new ArrayList<>();
        labelList.add("diease");
        labelList.add("nodiese");
        return labelList;
    }

    @Override
    protected int getImageSizeX() {
        return 224;
    }

    @Override
    protected int getImageSizeY() {
        return 224;
    }

    @Override
    protected void addPixelValue(int pixelValue) {
        imgData.putFloat(((pixelValue >> 16) & 0xFF) / 255.f);
        imgData.putFloat(((pixelValue >> 8) & 0xFF) / 255.f);
        imgData.putFloat((pixelValue & 0xFF) / 255.f);
    }

}
