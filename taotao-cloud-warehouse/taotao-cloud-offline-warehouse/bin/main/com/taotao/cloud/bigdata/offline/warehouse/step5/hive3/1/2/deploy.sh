schematool -dbType mysql -initSchema

nohup hive --service metastore &
nohup hive --service hiveserver2 &

hive --hiveconf hive.root.logger=DEBUG,console

beeline -i ~/.hiverc -u jdbc:hive2://127.0.0.1:10000 -n root
