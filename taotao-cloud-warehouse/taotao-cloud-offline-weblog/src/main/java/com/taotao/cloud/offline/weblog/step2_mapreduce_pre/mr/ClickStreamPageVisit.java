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

import com.taotao.cloud.bigdata.offline.weblog.step2_mapreduce_pre.mrbean.PageViewsBean;
import com.taotao.cloud.bigdata.offline.weblog.step2_mapreduce_pre.mrbean.VisitBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 * @Author ClickStreamPageVisit
 * @Date 18-8-11
 * @Version V1.0.0
 * @Description:
 */
public class ClickStreamPageVisit {

    /**
     * ClickStreamPageVisitMapper
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    static class ClickStreamPageVisitMapper extends Mapper<LongWritable, Text, Text, PageViewsBean> {

        PageViewsBean pageViewsBean = new PageViewsBean();
        Text k = new Text();

        @Override
        protected void map( LongWritable key, Text value, Context context )
                throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split(",");

            String step = fields[5];
            pageViewsBean.set(
                    fields[0], fields[1], fields[2], fields[3], fields[4], step, fields[6],
                    fields[7], fields[8], fields[9]);
            k.set(pageViewsBean.getSession());
            context.write(k, pageViewsBean);
        }
    }

    /**
     * ClickStreamPageVisitReducer
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    static class ClickStreamPageVisitReducer extends Reducer<Text, PageViewsBean, NullWritable, VisitBean> {

        NullWritable k = NullWritable.get();
        Text v = new Text();

        @Override
        protected void reduce( Text session, Iterable<PageViewsBean> pvBeans, Context context )
                throws IOException, InterruptedException {
            // 将pvBeans按照step排序
            ArrayList<PageViewsBean> beans = new ArrayList<>();
            for (PageViewsBean pvBean : pvBeans) {
                PageViewsBean pvb = new PageViewsBean();
                try {
                    BeanUtils.copyProperties(pvb, pvBean);
                    beans.add(pvb);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Collections.sort(
                    beans,
                    new Comparator<PageViewsBean>() {
                        @Override
                        public int compare( PageViewsBean o1, PageViewsBean o2 ) {
                            return Integer.parseInt(o1.getStep()) > Integer.parseInt(o2.getStep())
                                    ? 1
                                    : -1;
                        }
                    });

            // 取这次visit的首尾pageview记录，将数据放入VisitBean中
            VisitBean visitBean = new VisitBean();

            // 取visit的首记录
            visitBean.setInPage(beans.get(0).getRequest_url());
            visitBean.setInTime(beans.get(0).getTimestr());

            // 取visit的尾记录
            visitBean.setOutPage(beans.get(beans.size() - 1).getRequest_url());
            visitBean.setOutTime(beans.get(beans.size() - 1).getTimestr());

            // visit访问的页面数
            visitBean.setPageVisits(beans.size());

            // 来访者的ip
            visitBean.setRemote_addr(beans.get(0).getRemote_addr());

            // 本次visit的referal
            visitBean.setReferal(beans.get(0).getReferal());
            visitBean.setSession(session.toString());

            context.write(k, visitBean);
        }
    }

    public static void main( String[] args ) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(ClickStreamPageVisit.class);

        job.setMapperClass(ClickStreamPageVisitMapper.class);
        job.setReducerClass(ClickStreamPageVisitReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(PageViewsBean.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(VisitBean.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
