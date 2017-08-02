package main.test;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Since2017/6/29 ZhaCongJie@HF
 */
public class coreJava {
    public static void main(String[] args){
        System.out.println(test());
    }

    private static String test() {
        try {
            return "1";
        } catch (Exception e) {
            return "2";
        } finally {
            System.out.println("333");
        }
    }


    @Test
    public void test1(){
        System.out.println(test());
    }
    @Test
    public void test2(){
//        List<String> list = new ArrayList<String>(5);
//        System.out.println(list.get(4));
        Integer a = null;
        System.out.println(1+a);
    }
    @Test
    public void test3(){
        String[] str = new String[3];
        System.out.println(str[1]);
    }

    @Test
    public void test4(){
        String str = "1.5";
//        Math.floor(NumberUtils.toDouble(str));
        System.out.println((int)Math.floor(NumberUtils.toDouble(str)));
        System.out.println((int)Math.ceil(NumberUtils.toDouble(str)));
        System.out.println((int)Math.rint(NumberUtils.toDouble(str)));
        System.out.println(Math.round(NumberUtils.toDouble(str)));

    }

    @Test
    public void test5(){
        String[] arr = new String[]{"corpid","loginname","cid","timestamp","progress_cb_url","sign"};
        Arrays.sort(arr);
        System.out.println(arr);

    }
}



