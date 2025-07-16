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

package com.taotao.cloud.hive.udaf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFParameterInfo;
import org.apache.hadoop.hive.ql.util.JavaDataModel;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.LongObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.io.LongWritable;

/**
 * 开发通用UDAF有两个步骤：
 * <p>
 * 第一个是编写resolver类，第二个是编写evaluator类
 * <p>
 * resolver负责类型检查，操作符重载
 * <p>
 * evaluator真正实现UDAF的逻辑。
 * <p>
 * 通常来说，顶层UDAF类继承{@link org.apache.hadoop.hive.ql.udf.generic.GenericUDAFResolver2}
 * <p>
 * 里面编写嵌套类evaluator实现UDAF的逻辑
 * <p>
 * resolver通常继承org.apache.hadoop.hive.ql.udf.GenericUDAFResolver2，但是更建议继承AbstractGenericUDAFResolver，隔离将来hive接口的变化
 * <p>
 * GenericUDAFResolver和GenericUDAFResolver2接口的区别是:  后面的允许evaluator实现利用GenericUDAFParameterInfo可以访问更多的信息，例如DISTINCT限定符，通配符(*)。
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 17:19
 */
public class CountUDAF extends AbstractGenericUDAFResolver {

    /**
     * 构建方法，传入的是函数指定的列
     *
     * @param params
     * @throws SemanticException
     */
    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] params) throws SemanticException {
        if (params.length > 1) {
            throw new UDFArgumentLengthException("Exactly one argument is expected");
        }
        return new CountUDAFEvaluator();
    }

    /**
     * 这个构建方法可以判输入的参数是*号或者distinct
     *
     * @param info
     * @return
     * @throws SemanticException
     */
    @Override
    public GenericUDAFEvaluator getEvaluator(GenericUDAFParameterInfo info)
            throws SemanticException {

        ObjectInspector[] parameters = info.getParameterObjectInspectors();
        boolean isAllColumns = false;
        if (parameters.length == 0) {
            if (!info.isAllColumns()) {
                throw new UDFArgumentException("Argument expected");
            }

            if (info.isDistinct()) {
                throw new UDFArgumentException("DISTINCT not supported with");
            }
            isAllColumns = true;
        } else if (parameters.length != 1) {
            throw new UDFArgumentLengthException("Exactly one argument is expected.");
        }
        return new CountUDAFEvaluator(isAllColumns);
    }

    /**
     * GenericUDAFEvaluator类实现UDAF的逻辑
     * <p>
     * enum Mode运行阶段枚举类 PARTIAL1； 这个是mapreduce的map阶段：从原始数据到部分数据聚合 将会调用iterate()和terminatePartial()
     * <p>
     * PARTIAL2: 这个是mapreduce的map端的Combiner阶段，负责在map端合并map的数据：部分数据聚合 将会调用merge()和terminatePartial()
     * <p>
     * FINAL: mapreduce的reduce阶段：从部分数据的聚合到完全聚合 将会调用merge()和terminate()
     * <p>
     * COMPLETE: 如果出现了这个阶段，表示mapreduce只有map，没有reduce，所以map端就直接出结果了；从原始数据直接到完全聚合
     * 将会调用iterate()和terminate()
     */
    public static class CountUDAFEvaluator extends GenericUDAFEvaluator {

        private boolean isAllColumns = false;

        /**
         * 合并结果的类型
         */
        private LongObjectInspector aggOI;

        private LongWritable result;

        public CountUDAFEvaluator() {}

        public CountUDAFEvaluator(boolean isAllColumns) {
            this.isAllColumns = isAllColumns;
        }

        /**
         * 负责初始化计算函数并设置它的内部状态，result是存放最终结果的
         *
         * @param m          代表此时在map-reduce哪个阶段，因为不同的阶段可能在不同的机器上执行，需要重新创建对象partial1，partial2，final，complete
         * @param parameters partial1或complete阶段传入的parameters类型是原始输入数据的类型 partial2和final阶段（执行合并）的parameters类型是partial-aggregations（既合并返回结果的类型），此时parameters长度肯定只有1了
         * @return ObjectInspector 在partial1和partial2阶段返回局部合并结果的类型，既terminatePartial的类型
         * 在complete或final阶段返回总结果的类型，既terminate的类型
         * @throws HiveException
         */
        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
            super.init(m, parameters);
            // 当是combiner和reduce阶段时，获取合并结果的类型，因为需要执行merge方法
            // merge方法需要部分合并的结果类型来取得值
            if (m == Mode.PARTIAL2 || m == Mode.FINAL) {
                aggOI = (LongObjectInspector) parameters[0];
            }

            // 保存总结果
            result = new LongWritable(0);
            // 局部合并结果的类型和总合并结果的类型都是long
            return PrimitiveObjectInspectorFactory.writableLongObjectInspector;
        }

        /**
         * 定义一个AbstractAggregationBuffer类来缓存合并值
         */
        static class CountAgg extends AbstractAggregationBuffer {

            long value;

            /**
             * 返回类型占的字节数，long为8
             *
             * @return
             */
            @Override
            public int estimate() {
                return JavaDataModel.PRIMITIVES2;
            }
        }

        /**
         * 创建缓存合并值的buffer
         *
         * @return
         * @throws HiveException
         */
        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            CountAgg countAgg = new CountAgg();
            reset(countAgg);
            return countAgg;
        }

        /**
         * 重置合并值
         *
         * @param agg
         * @throws HiveException
         */
        @Override
        public void reset(AggregationBuffer agg) throws HiveException {
            ((CountAgg) agg).value = 0;
        }

        /**
         * map时执行，迭代数据
         *
         * @param agg
         * @param parameters
         * @throws HiveException
         */
        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {
            // parameters为输入数据
            // parameters == null means the input table/split is empty
            if (parameters == null) {
                return;
            }
            if (isAllColumns) {
                ((CountAgg) agg).value++;
            } else {
                boolean countThisRow = true;
                for (Object nextParam : parameters) {
                    if (nextParam == null) {
                        countThisRow = false;
                        break;
                    }
                }
                if (countThisRow) {
                    ((CountAgg) agg).value++;
                }
            }
        }

        /**
         * 返回buffer中部分聚合结果，map结束和combiner结束执行
         *
         * @param agg
         * @throws HiveException
         */
        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            return terminate(agg);
        }

        /**
         * 合并结果，combiner或reduce时执行
         *
         * @param agg
         * @param partial
         * @throws HiveException
         */
        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {
            if (partial != null) {
                // 累加部分聚合的结果
                ((CountAgg) agg).value += aggOI.get(partial);
            }
        }

        /**
         * 返回buffer中总结果，reduce结束执行或者没有reduce时map结束执行
         *
         * @param agg
         * @return
         * @throws HiveException
         */
        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            // 每一组执行一次（group by）
            result.set(((CountAgg) agg).value);
            // 返回writable类型
            return result;
        }
    }
}
