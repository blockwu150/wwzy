package com.enation.app.javashop.framework.util;

import junit.framework.TestCase;
import org.junit.Test;

public class HttpUtilsTest extends TestCase {


    @Test
    public void testGet(){
        byte[] urlByts = NetImageUtil.getImageFromNetByUrl("http://javashop-statics.oss-cn-beijing.aliyuncs.com/demo/1DCBCAFDD1464F27A9311AC4710862C3.jpg_300x300");
        String base64Image = Base64.encode(urlByts);
        System.out.println("base64 code is : ");
        System.out.println(base64Image);
    }

}