package com.example.plex.vfix.utils;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Plex on 2017/9/10.
 */

public class Utils {

    public static String generateUrlString(Map<String,String> map){

        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (Iterator< Map.Entry<String,String> > i = map.entrySet().iterator();i.hasNext();){
            Map.Entry<String,String> value = i.next();
            sb.append(value.getKey()+"="+ URLEncoder.encode(value.getValue()));
            if(i.hasNext()){
                sb.append("&");
            }
        }
        return sb.toString();
    }


}
