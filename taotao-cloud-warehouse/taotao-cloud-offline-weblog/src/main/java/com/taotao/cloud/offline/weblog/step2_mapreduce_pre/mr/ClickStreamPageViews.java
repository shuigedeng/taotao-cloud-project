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

package com.taotao.cloud.offline.weblog.step2_mapreduce_pre.mr;

import com.taotao.cloud.bigdata.offline.weblog.step2_mapreduce_pre.mrbean.WebLogBean;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @ClassName: step2_mapreduce_pre.com.bigdata.log.click.mr
 * @Author ClickStreamPageViews
 * @Date 18-8-10
 * @Version V1.0.0
 * @Description:
 */
public class ClickStreamPageViews {

    static class PageViewsStreamThreeMapper extends Mapper<LongWritable, Text, Text, WebLogBean> {
        Text k = new Text();
        WebLogBean v = new WebLogBean();

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split(",");

            if (fields.length < 9) {
                return;
            }

            v.set(
                    "true".equals(fields[0]),
                    fields[1],
                    fields[2],
                    fields[3],
                    fields[4],
                    fields[5],
                    fields[6],
                    fields[7],
                    fields[8]);

            if (v.isValid()) {
                k.set(v.getRemote_addr());
                context.write(k, v);
            }
        }
    }

    static class PageViewsStreamThreeReducer extends Reducer<Text, WebLogBean, NullWritable, Text> {

        @Override
        protected void reduce(Text key, Iterable<WebLogBean> values, Context context)
                throws IOException, InterruptedException {
            List<WebLogBean> beans = new ArrayList<>();
            Text v = new Text();
            NullWritable k = NullWritable.get();

            try {
                for (WebLogBean bean : values) {
                    WebLogBean webLogBean = new WebLogBean();
                    try {
                        BeanUtils.copyProperties(webLogBean, bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ;
                    beans.add(webLogBean);
                }

                // 将bean按时间先后顺序排序
                Collections.sort(
                        beans,
                        new Comparator<WebLogBean>() {
                            @Override
                            public int compare(WebLogBean o1, WebLogBean o2) {
                                try {
                                    Date date = toDate(o1.getRemote_time_local());
                                    Date date2 = toDate(o2.getRemote_time_local());
                                    if (null == date || null == date2) {
                                        return 0;
                                    }
                                    return date.compareTo(date2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });

                /*
                 * 以下逻辑为：从有序bean中分辨出各次visit，并对一次visit中所访问的page按顺序标号step
                 */
                int step = 1;
                String session = UUID.randomUUID().toString();

                for (int i = 0; i < beans.size(); i++) {
                    WebLogBean bean = beans.get(i);

                    // 如果仅有1条数据，则直接输出
                    if (1 == beans.size()) {
                        v.set(
                                session
                                        + ","
                                        + key.toString()
                                        + ","
                                        + bean.getRemote_user()
                                        + ","
                                        + bean.getRemote_time_local()
                                        + ","
                                        + bean.getRequest_method_url()
                                        + ","
                                        + step
                                        + ","
                                        + (60)
                                        + ","
                                        + bean.getHttp_referer()
                                        + ","
                                        + bean.getHttp_user_agent()
                                        + ","
                                        + bean.getReponse_body_bytes()
                                        + ","
                                        + bean.getStatus());
                        context.write(k, v);
                        session = UUID.randomUUID().toString();
                        break;
                    }

                    // 如果不止1条数据，则将第一条跳过不输出，遍历第二条时再输出
                    if (i == 0) {
                        continue;
                    }

                    // 求近两次时间差
                    long time =
                            timeDiff(
                                    toDate(bean.getRemote_time_local()),
                                    toDate(beans.get(i - 1).getRemote_time_local()));

                    // 如果本次-上次时间差<30分钟，则输出前一次的页面访问信息
                    if (time < 30 * 60 * 1000) {
                        v.set(
                                session
                                        + ","
                                        + key.toString()
                                        + ","
                                        + beans.get(i - 1).getRemote_user()
                                        + ","
                                        + beans.get(i - 1).getRemote_time_local()
                                        + ","
                                        + beans.get(i - 1).getRequest_method_url()
                                        + ","
                                        + step
                                        + ","
                                        + (time / 1000)
                                        + ","
                                        + beans.get(i - 1).getHttp_referer()
                                        + ","
                                        + beans.get(i - 1).getHttp_user_agent()
                                        + ","
                                        + beans.get(i - 1).getReponse_body_bytes()
                                        + ","
                                        + beans.get(i - 1).getStatus());
                        context.write(k, v);
                        step++;
                    } else {
                        // 如果本次-上次时间差>30分钟，则输出前一次的页面访问信息且将step重置，以分隔为新的visit
                        v.set(
                                session
                                        + ","
                                        + key.toString()
                                        + ","
                                        + beans.get(i - 1).getRemote_user()
                                        + ","
                                        + beans.get(i - 1).getRemote_time_local()
                                        + ","
                                        + beans.get(i - 1).getRequest_method_url()
                                        + ","
                                        + (step)
                                        + ","
                                        + (60)
                                        + ","
                                        + beans.get(i - 1).getHttp_referer()
                                        + ","
                                        + beans.get(i - 1).getHttp_user_agent()
                                        + ","
                                        + beans.get(i - 1).getReponse_body_bytes()
                                        + ","
                                        + beans.get(i - 1).getStatus());
                        context.write(k, v);
                        // 输出完上一条之后，重置step编号
                        step = 1;
                        session = UUID.randomUUID().toString();
                    }

                    // 如果此次遍历的是最后一条，则将本条直接输出
                    if (beans.size() - 1 == i) {
                        // 设置默认停留市场为60s
                        v.set(
                                session
                                        + ","
                                        + key.toString()
                                        + ","
                                        + bean.getRemote_user()
                                        + ","
                                        + bean.getRemote_time_local()
                                        + ","
                                        + bean.getRequest_method_url()
                                        + ","
                                        + step
                                        + ","
                                        + (60)
                                        + ","
                                        + bean.getHttp_referer()
                                        + ","
                                        + bean.getHttp_user_agent()
                                        + ","
                                        + bean.getReponse_body_bytes()
                                        + ","
                                        + bean.getStatus());
                        context.write(k, v);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        private long timeDiff(Date time1, Date time2) throws ParseException {
            return time1.getTime() - time2.getTime();
        }

        private Date toDate(String timeStr) throws ParseException {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            return df.parse(timeStr);
        }
    }

    public static void main(String[] args)
            throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(ClickStreamPageViews.class);

        job.setMapperClass(PageViewsStreamThreeMapper.class);
        job.setReducerClass(PageViewsStreamThreeReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(WebLogBean.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
