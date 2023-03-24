package com.viktor.kh.dev.shoplist.utils;

import java.util.Random;

public class MyRandom {

    public static int random(int number){
        Random random = new Random();
        return random.nextInt(number);
    }
}
