package com.asiainfo.dtdt.common;

import java.util.UUID;

public class UuidUtil {

    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
    
    public static void main(String[] args)
    {
    	System.out.println(generateUUID());
    }
}
