#!/bin/bash

#多个空格只保留一个
#sed '/./,/^$/!d' test

#删除开头的空白行
#sed '/./,$!d' test

#删除结尾的空白行
sed '{
:start
/^\n*$/{$d; N; b start}
}' test

#删除html标签
#有问题
#s/<.*>//g

#sed 's/<[^>]*>//g' test1

#sed 's/<[^>]*>//g;/^$/d' test1


#and符号，代表替换命令中的匹配模式，不管预定义模式是什么文本，都可以用and符号替换，and符号会提取匹配替换命令中指定替换模式中的所有字符串
echo "The cat sleeps in his hat" | sed 's/.at/"&"/g'

#替换单独的单词
echo "The System Administrator manual" | sed 's/\(System\) Administrator/\1 user/'

#在长数字中插入逗号
echo "1234567" | sed '{:start; s/\(.*[0-9]\)\([0-9]\{3\}\)/\1,\2/; t start}'

#给文件中的行编号
sed '=' test | sed 'N; s/\n/ /'
