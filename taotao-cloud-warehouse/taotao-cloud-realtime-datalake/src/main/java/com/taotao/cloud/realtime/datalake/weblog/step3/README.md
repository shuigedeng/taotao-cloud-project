### 准实时(1分钟一个微批次)采集数据到hadoop中

### @see taotao-cloud-bigdata -> taotao-cloud-hive 项目

cd ${HIVE_home}/auxlib
cp hudi-hadoop-mr-bundle-0.6.2-incubating.jar .

set hive.input.format=org.apache.hudi.hadoop.hive.HoodieCombinerHiveInputFormat

cp hudi-presto-bundle-0.6.2-incubating.jar .

cp hudi-spark-bundle-2.12-0.6.2-incubating.jar .

打包

上传jar包到spark集群

运行jar包

**目的: 数据源:kafka 通过spark streaming 以及hudi库 生成hudi表和hive表 将数据落在hadoop中**
