package com.taotao.cloud.recommend.biz.service;

import com.taotao.cloud.recommend.biz.dto.ItemDTO;
import com.taotao.cloud.recommend.biz.dto.RelateDTO;
import com.taotao.cloud.recommend.biz.dto.UserDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Data
@Slf4j
public class FileDataSource {

      public  static String folderPath;

    /**
     * 方法描述: 读取基础数据
     *
     * @Return {@link List<RelateDTO>}
     * @author tarzan
     * @date 2020年07月31日 16:53:40
     */
    public static List<RelateDTO> getData() {
        folderPath= Objects.requireNonNull(FileDataSource.class.getResource("/ml-100k")).getPath();
        List<RelateDTO> relateList = Lists.newArrayList();
        try {
            FileInputStream out = new FileInputStream(folderPath+"\\u.data");
            InputStreamReader reader = new InputStreamReader(out, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                String newline = line.replaceAll("[\t]", " ");
                String[] ht = newline.split(" ");
                Integer userId = Integer.parseInt(ht[0]);
                Integer movieId = Integer.parseInt(ht[1]);
                Integer rating = Integer.parseInt(ht[2]);
                RelateDTO dto = new RelateDTO(userId, movieId, rating);
                relateList.add(dto);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return relateList;
    }

    /**
     * 方法描述: 读取用户数据
     *
     * @Return {@link List<UserDTO>}
     * @author tarzan
     * @date 2020年07月31日 16:54:51
     */
    public static List<UserDTO> getUserData() {
        folderPath= Objects.requireNonNull(FileDataSource.class.getResource("/ml-100k")).getPath();
        List<UserDTO> userList = Lists.newArrayList();
        try {
            FileInputStream out = new FileInputStream(folderPath+"\\u.user");
            InputStreamReader reader = new InputStreamReader(out, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                String newline = line.replaceAll("[\t]", " ");
                String[] ht = newline.split("\\|");
                Integer id = Integer.parseInt(ht[0]);
                Integer age = Integer.parseInt(ht[1]);
                String sex = ht[2];
                String profession = ht[3];
                String postcode = ht[4];
                UserDTO dto = new UserDTO(id, age, sex, profession, postcode);
                userList.add(dto);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return userList;
    }


    /**
     * 方法描述: 读取电影数据
     *
     * @Return {@link List<ItemDTO>}
     * @author tarzan
     * @date 2020年07月31日 16:54:22
     */
    public static List<ItemDTO> getItemData() {
        folderPath= Objects.requireNonNull(FileDataSource.class.getResource("/ml-100k")).getPath();
        List<ItemDTO> itemList = Lists.newArrayList();
        try {
            FileInputStream out = new FileInputStream(folderPath+"\\u.item");
            InputStreamReader reader = new InputStreamReader(out, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                String newline = line.replaceAll("[\t]", " ");
                String[] ht = newline.split("\\|");
                Integer id = Integer.parseInt(ht[0]);
                String name = ht[1];
                String date = ht[2];
                String link = ht[3];
                ItemDTO dto = new ItemDTO(id, name, date, link);
                itemList.add(dto);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return itemList;
    }


}

