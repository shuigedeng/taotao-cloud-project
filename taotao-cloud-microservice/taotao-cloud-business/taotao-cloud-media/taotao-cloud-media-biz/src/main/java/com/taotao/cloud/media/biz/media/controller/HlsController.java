/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.media.biz.media.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.media.biz.media.common.AjaxResult;
import com.taotao.cloud.media.biz.media.dto.CameraDto;
import com.taotao.cloud.media.biz.media.entity.Camera;
import com.taotao.cloud.media.biz.media.mapper.CameraMapper;
import com.taotao.cloud.media.biz.media.service.HlsService;
import com.taotao.cloud.media.biz.media.vo.CameraVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** hls接口 */
@RestController
public class HlsController {

    @Autowired
    private HlsService hlsService;

    @Autowired
    private CameraMapper cameraMapper;

    /**
     * ts接收接口（回传，这里只占用网络资源，避免使用硬盘资源）
     *
     * @param request
     */
    @RequestMapping("record/{mediaKey}/{tsname}")
    public void name(
            HttpServletRequest request,
            @PathVariable("mediaKey") String mediaKey,
            @PathVariable("tsname") String tsname) {

        try {
            if (tsname.indexOf("m3u8") != -1) {
                hlsService.processHls(mediaKey, request.getInputStream());
            } else {
                hlsService.processTs(mediaKey, tsname, request.getInputStream());
            }
        } catch (IORuntimeException e) {
            LogUtils.error(e);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * @param mediaKey
     */
    @RequestMapping("ts/{cameraId}/{tsName}")
    public void getts(
            HttpServletResponse response,
            @PathVariable("cameraId") String mediaKey,
            @PathVariable("tsName") String tsName)
            throws IOException {

        String tsKey = mediaKey.concat("-").concat(tsName);
        byte[] bs = HlsService.cacheTs.get(tsKey);
        if (null == bs) {
            response.setContentType("application/json");
            response.getOutputStream().write("尚未生成ts".getBytes("utf-8"));
            response.getOutputStream().flush();
            return;
        } else {
            response.getOutputStream().write(bs);
            response.setContentType("video/mp2t");
            response.getOutputStream().flush();
        }
    }

    /**
     * hls播放接口
     *
     * @throws IOException
     */
    @RequestMapping("hls")
    public void video(CameraDto cameraDto, HttpServletResponse response) throws IOException {
        if (StrUtil.isBlank(cameraDto.getUrl())) {
            response.setContentType("application/json");
            response.getOutputStream().write("参数有误".getBytes("utf-8"));
            response.getOutputStream().flush();
        } else {
            String mediaKey = MD5.create().digestHex(cameraDto.getUrl());
            byte[] hls = HlsService.cacheM3u8.get(mediaKey);
            if (null == hls) {
                response.setContentType("application/json");
                response.getOutputStream().write("尚未生成m3u8".getBytes("utf-8"));
                response.getOutputStream().flush();
            } else {
                response.setContentType("application/vnd.apple.mpegurl"); // application/x-mpegURL //video/mp2t ts;
                response.getOutputStream().write(hls);
                response.getOutputStream().flush();
            }
        }
    }

    /**
     * 关闭切片
     *
     * @param cameraVo
     * @return
     */
    @RequestMapping("stopHls")
    public AjaxResult stop(CameraVo cameraVo) {
        String digestHex = MD5.create().digestHex(cameraVo.getUrl());
        CameraDto cameraDto = new CameraDto();
        cameraDto.setUrl(cameraVo.getUrl());
        cameraDto.setMediaKey(digestHex);

        Camera camera = new Camera();
        camera.setHls(0);
        QueryWrapper<Camera> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("media_key", digestHex);
        int res = cameraMapper.update(camera, queryWrapper);

        hlsService.closeConvertToHls(cameraDto);
        return AjaxResult.success("停止切片成功");
    }

    /**
     * 开启切片
     *
     * @param cameraVo
     * @return
     */
    @RequestMapping("startHls")
    public AjaxResult start(CameraVo cameraVo) {
        String digestHex = MD5.create().digestHex(cameraVo.getUrl());
        CameraDto cameraDto = new CameraDto();
        cameraDto.setUrl(cameraVo.getUrl());
        cameraDto.setMediaKey(digestHex);

        boolean startConvertToHls = hlsService.startConvertToHls(cameraDto);

        if (startConvertToHls) {
            Camera camera = new Camera();
            QueryWrapper<Camera> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("media_key", digestHex);
            camera.setHls(1);
            int res = cameraMapper.update(camera, queryWrapper);
        }

        return AjaxResult.success("开启切片成功", startConvertToHls);
    }
}
