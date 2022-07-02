
# 前言
Excel、Word基于poi导出；Pdf基于itextpdf导出；zip基于jdk导出。  
所有导出Exporter支持写到**给定输出流**writeTo() 、写到**servlet输出流**writeToServletResponse() 两种方式，以下所有测试用例均输出到给定输出流。   
所有测试用例数据：[对象](./src/test/java/cn/wisewe/docx4j/output/builder/Person.java)、[数据](./src/test/java/cn/wisewe/docx4j/output/builder/SpecDataFactory.java)   

# Excel导出
支持Excel **动态表格数据**、**表格单元格合并**、**数据单元格合并**、**动态Sheet** 、**行列冻结**、**图片单元格支持**等，可以快速导出基础的表格，并设定了一些基础表格样式。

[查看excel导出用法](./spread-sheet.md)

# Word导出

支持Word **动态表格数据**、**动态段落数据局**、**表格单元格合并**、**数据单元格合并**、**动态文档页** 、**页眉页脚**、**图片支持**等，可以快速导出基础的word文档，并设定了一些基础文档样式。

[查看word导出用法](./document.md)

# Pdf导出
支持Pdf **动态表格数据**、**动态段落数据局**、**表格单元格合并**、**数据单元格合并**、**动态文档页** 、**页眉页脚**、**图片支持**、**图片水印**、**文字水印**，可以快速导出基础的pdf文档，并设定了一下基础的pdf样式。

[查看pdf导出用法](./portable.md)

# zip压缩包导出

支持压缩包 **自定义目录**、**硬盘文件/文件夹写入**、**内存文件直接写入(可以直接使用本工具生成的excel、word、pdf、zip)**，可以快速导出任意目录结构的压缩包。

[查看zip导出用法](./compression.md)