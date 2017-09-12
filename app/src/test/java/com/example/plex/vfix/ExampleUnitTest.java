package com.example.plex.vfix;

import com.example.plex.vfix.beans.FixDetailBean;
import com.example.plex.vfix.beans.FixListResultBean;
import com.example.plex.vfix.beans.OrdinaryResultBean;
import com.example.plex.vfix.beans.ProfileBean;
import com.example.plex.vfix.beans.ProfileResultBean;
import com.example.plex.vfix.utils.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String json = "{\"code\":1,\"msg\":\"获取待解决报修记录成功\",\"data\":[{\"id\":1,\"user_id\":2,\"problem\":\"交换机被雷劈了\",\"desc\":null,\"build_type\":null,\"build_number\":null,\"address\":null,\"resolve\":null,\"repairer_id\":null,\"repairer_name\":null,\"start_time\":\"2017-09-10 01:00:23\",\"update_time\":\"2017-09-11 09:29:46\",\"status\":0},{\"id\":3,\"user_id\":2,\"problem\":\"交换机被雷劈了\",\"desc\":\"交换机被雷劈了\",\"build_type\":\"职工楼\",\"build_number\":\"1\",\"address\":\"教30三单元501\",\"resolve\":null,\"repairer_id\":4,\"repairer_name\":null,\"start_time\":\"2017-09-10 01:12:05\",\"update_time\":\"2017-09-11 09:13:28\",\"status\":1},{\"id\":4,\"user_id\":2,\"problem\":\"交换机被雷劈了\",\"desc\":\"交换机被雷劈了\",\"build_type\":\"职工楼\",\"build_number\":\"1\",\"address\":\"教30三单元501\",\"resolve\":null,\"repairer_id\":null,\"repairer_name\":null,\"start_time\":\"2017-09-10 11:42:37\",\"update_time\":\"2017-09-11 09:29:42\",\"status\":0},{\"id\":5,\"user_id\":2,\"problem\":\"交换机被雷劈了\",\"desc\":\"交换机被雷劈了\",\"build_type\":\"职工楼\",\"build_number\":\"1\",\"address\":\"教30三单元501\",\"resolve\":null,\"repairer_id\":null,\"repairer_name\":null,\"start_time\":\"2017-09-10 11:42:40\",\"update_time\":\"2017-09-11 09:29:41\",\"status\":0}]}";
        String json2= "{\"code\":1,\"msg\":\"获取待解决报修记录成功\",\"data\":null}";
        Gson gson = new GsonBuilder().create();

       // FixListResultBean ben1 = gson.fromJson(json2,FixListResultBean.class);

       //System.out.println(ben1.getData());

        OrdinaryResultBean bnbb = gson.fromJson(json2,OrdinaryResultBean.class);



    }

    @Test
    public void profileTest() throws Exception{

        String url = Configuration.SERVER_HOST +"user/profile/userInfo";
        Headers headers = new Headers.Builder().add("XX-Token","b4ac42a89ca444be3528e81ce84987f55ddc018f227bc716f82d40c36ee9be66")
                                               .add("XX-Device-Type","mobile")
                                               .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).headers(headers).build();
        Response response = client.newCall(request).execute();
        Gson gson = new GsonBuilder().create();

        ProfileResultBean b = gson.fromJson(response.body().string(),ProfileResultBean.class);

        System.out.println(b.getData().getNickname());

    }
}