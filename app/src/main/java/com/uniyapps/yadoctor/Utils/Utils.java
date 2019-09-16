package com.uniyapps.yadoctor.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Utils {

    public static int  BLOOD = 0 ,  PAIN=0,H=0,AGE=0;

    public Utils(){}
    public static void showProgressBarDialog(){}
    public static void hideProgressBarDialog(){}
    public static void zero(){
        BLOOD = 0;
        PAIN = 0;
        H = 0 ;
        AGE = 0 ; 
    }

    public static LinkedHashMap<String, Integer> sortMap(LinkedHashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> capitalList = new LinkedList<>(map.entrySet());

        Collections.sort(capitalList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : capitalList)
        {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}
