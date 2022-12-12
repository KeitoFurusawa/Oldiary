package com.example.oldiary;

import java.util.HashMap;

public class ExpandablelistDataPump {

    public static HashMap<String, String> getData() {
        HashMap<String, String> expandableListDetail = new HashMap<String, String>();

        expandableListDetail.put("Parent 1", "child 1");
        expandableListDetail.put("Parent 2", "child 2");
        expandableListDetail.put("Parent 3", "child 3");
        expandableListDetail.put("Parent 4", "child 4");
        expandableListDetail.put("Parent 5", "child 5");
        expandableListDetail.put("Parent 6", "child 6");
        expandableListDetail.put("Parent 7", "child 7");
        expandableListDetail.put("Parent 8", "child 8");
        expandableListDetail.put("Parent 9", "child 9");
        expandableListDetail.put("Parent 10", "child 10");

        return expandableListDetail;
    }
}
