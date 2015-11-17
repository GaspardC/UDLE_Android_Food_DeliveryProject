package ch.epfl.sweng.udle.activities;

import java.util.ArrayList;

/**
 * Created by Abdes on 01/11/2015.
 */
public class AdresseData {
    private ArrayList<String> resultList;
    private ArrayList<String> resultList_Id;

    AdresseData(ArrayList<String> resultList,ArrayList<String> resultList_Id){
        this.resultList = resultList;
        this.resultList_Id = resultList_Id;
    }
    ArrayList<String> getResultList(){
        return resultList;
    }
    ArrayList<String> getResultListId(){
        return resultList_Id;
    }
    void setResultList(ArrayList<String> resultList){
        this.resultList = resultList;
    }
    void setResultListId(ArrayList<String> resultList_Id){
        this.resultList_Id = resultList_Id;
    }
}
