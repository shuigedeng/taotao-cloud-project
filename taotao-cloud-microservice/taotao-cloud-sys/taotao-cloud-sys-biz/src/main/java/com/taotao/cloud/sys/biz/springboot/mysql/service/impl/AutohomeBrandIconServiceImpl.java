package com.taotao.cloud.sys.biz.springboot.mysql.service.impl;

import com.hrhx.springboot.domain.AutohomeBrand;
import com.hrhx.springboot.domain.AutohomeBrandIcon;
import com.hrhx.springboot.mysql.jpa.AutohomeBrandIconRepository;
import com.hrhx.springboot.mysql.service.AutohomeBrandIconService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutohomeBrandIconServiceImpl implements AutohomeBrandIconService {

    private static final OkHttpClient client = new OkHttpClient.Builder().build();
    @Autowired
    private AutohomeBrandIconRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getAllIconUrl(){
        List<AutohomeBrand> autohomeBrands = jdbcTemplate.query("SELECT car_brand_icon_url FROM autohome_brand GROUP BY car_brand_icon_url",new BeanPropertyRowMapper<>(AutohomeBrand.class));
        return autohomeBrands.stream().map(AutohomeBrand::getCarBrandIconUrl).collect(Collectors.toList());
    }

    public void updateIcon(){
        List<String> icons = getAllIconUrl();
        List<AutohomeBrandIcon> autohomeBrandIcons = new ArrayList<>();
        for (String icon : icons) {
            AutohomeBrandIcon autohomeBrandIcon = new AutohomeBrandIcon();
            autohomeBrandIcon.setCarBrandIconUrl(icon);
            autohomeBrandIcon.setCarBrandIconbyte(getIconByUrl(icon));
            autohomeBrandIcons.add(autohomeBrandIcon);

        }
        repository.save(autohomeBrandIcons);
    }

    private byte[] getIconByUrl(String url){
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().bytes();
        } catch (IOException e) {
//            e.printStackTrace();
            return new byte[]{};
        }
    }
}
