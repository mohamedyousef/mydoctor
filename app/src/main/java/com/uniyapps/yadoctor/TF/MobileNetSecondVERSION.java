package com.uniyapps.yadoctor.TF;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class MobileNetSecondVERSION extends ImageClassifierGeneral {

        public MobileNetSecondVERSION(Activity activity) {
            super(activity);
        }

        @Override
        protected String getModelPath() {
            return "mobilenet_075.lite";
        }

        @Override
        protected List<String> getLabelPath() {
            List<String> labelList = new ArrayList<>();
            labelList.add("notskin");
            labelList.add("skin");
            labelList.add("skinfar");
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
