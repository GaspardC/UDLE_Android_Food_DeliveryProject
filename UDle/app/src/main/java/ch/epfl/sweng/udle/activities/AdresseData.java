package ch.epfl.sweng.udle.activities;

import java.util.ArrayList;

/**
 * Created by Abdes on 01/11/2015.
 */
public class AdresseData {
    private ArrayList<String> resultList= null;
    private ArrayList<String> resultList_Id= null;

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
}
