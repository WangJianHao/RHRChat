package com.wjh.demo;

public class Test {
    public static void main(String[] args) {
        String  str = "{\"msgType\":10,\"sender\":0,\"message\":\"1\",\"time\":\"2020-10-21 16:43:58.0\"}{\"msgType\":10,\"sender\":0,\"message\":\"2\",\"time\":\"2020-10-21 16:43:59.0\"}{\"msgType\":10,\"sender\":0,\"message\":\"3\",\"time\":\"2020-10-21 16:44:00.0\"}";
        while (str.contains("}")) {
            System.out.println(str.indexOf("}"));
            System.out.println(str.substring(0, str.indexOf("}") + 1));
            if(str.indexOf("}")+1 > str.length()){
                break;
            }
            str = str.substring(str.indexOf("}") + 1);
        }
    }
}
