package com.uniyapps.yadoctor.TF;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.Delegate;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public abstract class ImageClassifierGeneral {
    /** Tag for the {@link Log}. */
    private static final String TAG = "MohamedYousefCamera";

    /** Number of results to show in the UI. */
    private static final int RESULTS_TO_SHOW = 1 ;
 ///   private   float  GOOD_PROPA = 0.3f;

    /** Dimensions of inputs. */
    private static final int DIM_BATCH_SIZE = 1;

    private static final int DIM_PIXEL_SIZE = 3;


    /* Preallocated buffers for storing image data in. */
    private int[] intValues = new int[getImageSizeX() * getImageSizeY()];

    /** An instance of the driver class to run model inference with Tensorflow Lite. */
    public Interpreter tflite = null;

    /** Labels corresponding to the output of the vision model. */
    private List<String> labelList;

    /** A ByteBuffer to hold image data, to be feed into Tensorflow Lite as inputs. */
    protected ByteBuffer imgData = null;
    Delegate gpuDelegate = null;

    /** An array to hold inference results, to be feed into Tensorflow Lite as outputs. */
    private float[][] labelProbArray = null;
    /** multi-stage low pass filter **/
    private float[][] filterLabelProbArray = null;
    private static final int FILTER_STAGES = 3;
    private static final float FILTER_FACTOR = 0.4f;
    Activity activity;
    Interpreter.Options options  = new Interpreter.Options();


    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });



    public ImageClassifierGeneral(Activity activity)  {
        this.activity = activity;
        labelList = getLabelPath();
        /*
        if (GpuDelegateHelper.isGpuDelegateAvailable()) {
            useGpu();
        }
        else*/
           useNNAPI();
          options.setNumThreads(12);

        try {
            tflite = new Interpreter(loadModelFile(activity),options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgData =
                ByteBuffer.allocateDirect(
                        4 * DIM_BATCH_SIZE * getImageSizeX() * getImageSizeY() * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());
        labelProbArray = new float[1][labelList.size()];
        filterLabelProbArray = new float[FILTER_STAGES][labelList.size()];
    }


    /** Classifies a frame from the preview stream. */
    public String classifyFrame(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, getImageSizeX(), getImageSizeY(), false);
        if (tflite == null) {
            try {
                tflite = new Interpreter(loadModelFile(activity),options);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        convertBitmapToByteBuffer(bitmap);
        tflite.run(imgData, labelProbArray);
        applyFilter();

        // print the results
        String textToShow = printTopKLabels();
        return textToShow+"";
    }

    private void applyFilter(){
        int num_labels =  labelList.size();

        // Low pass filter `labelProbArray` into the first stage of the filter.
        for(int j=0; j<num_labels; ++j){
            filterLabelProbArray[0][j] += FILTER_FACTOR*(labelProbArray[0][j] -
                    filterLabelProbArray[0][j]);
        }
        // Low pass filter each stage into the next.
        for (int i=1; i<FILTER_STAGES; ++i){
            for(int j=0; j<num_labels; ++j){
                filterLabelProbArray[i][j] += FILTER_FACTOR*(
                        filterLabelProbArray[i-1][j] -
                                filterLabelProbArray[i][j]);
            }
        }

        // Copy the last stage filter output back to `labelProbArray`.
        for(int j=0; j<num_labels; ++j){
            labelProbArray[0][j] = filterLabelProbArray[FILTER_STAGES-1][j];
        }
    }

    /** Closes tflite to release resources. */
    public void close() {
        tflite.close();
         tflite = null;
    }

    /** Memory-map the model file in Assets. */
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(getModelPath());
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;
        for (int i = 0; i < getImageSizeX(); ++i) {
            for (int j = 0; j < getImageSizeY(); ++j) {
                final int val = intValues[pixel++];
                addPixelValue(val);
            }
        }
    }
    /** Prints top-K labels, to be shown in UI as the results. */
    private  String printTopKLabels() {

        for (int i = 0; i < labelList.size(); ++i) {
            sortedLabels.add(
                    new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }

        // 4.2f make    0.598 = 0.6
        // convert string to arraylist or make confidance

        String textToShow = "";
        for (int i = 0; i < sortedLabels.size(); ++i) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            textToShow = label.getKey()+ textToShow;
        }
        return textToShow;
    }

    public void useNNAPI() {
        options.setUseNNAPI(true);
//        options.setUseNNAPI(false);
    }
    public void useGpu() {
            gpuDelegate = GpuDelegateHelper.createGpuDelegate();
            options.addDelegate(gpuDelegate);
    }

    protected abstract String getModelPath();
    protected abstract List<String> getLabelPath();
    protected abstract int getImageSizeX();
    protected abstract int getImageSizeY();
    protected abstract void addPixelValue(int pixelValue);

}
