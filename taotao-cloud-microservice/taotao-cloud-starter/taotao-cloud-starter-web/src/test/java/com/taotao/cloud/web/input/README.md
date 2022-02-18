# 前言
Excel基于poi导入；Zip基于jdk导入；  
所有导入均只接受**InputStream**，可自行设定在导入结束后是否关闭流。

# Excel导入

支持导入数据类型[参见](./src/main/java/cn/wisewe/docx4j/input/builder/sheet/CellSupportTypes.java)
结合hibernate-validator进行excel单元格值校验

[查看excel导入用法](./spread-sheet.md)

# Zip导入

可以对压缩包中的目录及文件进行进一步处理

[查看zip导入用法](./compression.md)