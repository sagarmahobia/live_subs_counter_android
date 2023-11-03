package com.sagar.livesubscounter.constants;

import com.sagar.livesubscounter.ApplicationScope;
import com.sagar.livesubscounter.network.models.sponsor.SpareKeys;

import java.util.LinkedList;
import java.util.Queue;

import javax.inject.Inject;

/**
 * Created by SAGAR MAHOBIA on 02-Dec-18. at 03:24
 */
@ApplicationScope
public class KeyStore {
 
    @Inject
    KeyStore() {
     
    //  Details removed to  prevent misuse
 
    }

    public String getKey() {
       
        return "##Removed";
    }

   
}

