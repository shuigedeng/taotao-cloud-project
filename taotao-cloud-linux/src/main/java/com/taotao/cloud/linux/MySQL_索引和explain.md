
    第 40 天【索引和explain(02)】

## MySQL中的索引

基本法则：索引应该构建在被用在查询条件的字段上；

- 索引类型：
    + B+ Tree索引：顺序存储，每一叶子节点到根节点的距离是相同的；左前缀索引，适合查找范围类的数据；
        * 可以使用 B-Tree 索引的查询类型：全键值、键值范围或键前缀查找；
            - 全值匹配：精确某个值，"Jinjiao King"；
            - 匹配最左前缀：只精确匹配起头部分，"Jin%"；
            - 匹配范围值：匹配某个范围的值，Age BETWEEN 20 AND 30； 
            - 精确匹配某一列并范围匹配另一列：多键索引使用，一键精确匹配，一键范围匹配；
            - 只访问索引的查询：索引即数据；
        * 不适合使用 B-Tree 索引的场景：
            - 如果不从最左列开始，索引无效；(Age,Name)
            - 不能跳过索引中的列；(StuID,Name,Age)
            - 如果查询中某个列是为范围查询，那么其右侧的列无法再使用索引优化查询：(StuID,Name)
    + Hash索引：基于哈希表实现，特别适用于精确匹配索引中的所有列；
        * 注意：只有Memory存储引擎支持显式hash索引；
        * 适用场景：
            - 只支持等值比较查询，包括 = ， IN(), <=>；
        * 不适合使用hash索引的场景：
            - 存储的非为值的顺序，因此，不适用于顺序查询；
            - 不支持模糊匹配；  
    + 空间索引（R-Tree）：
        * MyISAM支持空间索引；
    + 全文索引(FULLTEXT)：
        * 在文本中查找关键词：
- 索引优点：
    + 索引可以降低服务器需要扫描的数据量，减少了IO次数；
    + 索引可以帮助服务器避免排序和使用临时表；
    + 索引可以帮助将随机I/O转为顺序I/O；

- 高性能索引策略：
    + 独立使用列，尽量避免其参与运算；
    + 左前缀索引：索引构建于字段的左侧的多少个字符，要通过索引选择性来评估；
        * 索引选择性：不重复的索引值和数据表的记录总数的比值；
    + 多列索引：
        * AND操作时更适合使用多列索引；
    + 选择合适的索引列次序：将选择性最高的放左侧；
- 冗余和重复索引：
    + 不好的索引使用策略：
        * (Name), (Name,Age)

## 通过EXPLAIN来分析索引的有效性：

获取查询执行计划信息，用来查看查询优化器如何执行查询；

```
    EXPLAIN SELECT clause

        MariaDB [hellodb]> EXPLAIN SELECT Name FROM students WHERE StuID>10;
        +------+-------------+----------+------+---------------+------+---------+------+------+-------------+
        | id   | select_type | table    | type | possible_keys | key  | key_len | ref  | rows | Extra       |
        +------+-------------+----------+------+---------------+------+---------+------+------+-------------+
        |    1 | SIMPLE      | students | ALL  | PRIMARY       | NULL | NULL    | NULL |   27 | Using where |
        +------+-------------+----------+------+---------------+------+---------+------+------+-------------+
        1 row in set (0.00 sec)

    输出：
        id：当前查询语句中，每个SELECT语句的编号；
            复杂类型的查询有三种：
                简单子查询；
                用于FROM中的子查询；
                联合查询：UNION；

                注意：UNION查询的分析结果会出现一个额外匿名临时表；

        select_type：标明当前查询的类型；
            简单查询为SIMPLE
            复杂查询：
                SUBQUERY：简单子查询；
                    MariaDB [hellodb]> EXPLAIN SELECT Name,Age FROM students WHERE Age > (SELECT avg(Age) FROM students);
                    +------+-------------+----------+-------+---------------+------+---------+------+------+-------------+
                    | id   | select_type | table    | type  | possible_keys | key  | key_len | ref  | rows | Extra       |
                    +------+-------------+----------+-------+---------------+------+---------+------+------+-------------+
                    |    1 | PRIMARY     | students | range | Age           | Age  | 1       | NULL |    7 | Using where |
                    |    2 | SUBQUERY    | students | index | NULL          | Age  | 1       | NULL |   27 | Using index |
                    +------+-------------+----------+-------+---------------+------+---------+------+------+-------------+
                    2 rows in set (0.00 sec)
                DERIVED：用于FROM中的子查询；
                UNION：UNION语句的第一个之后的SELECT语句；
                UNION RESULT：匿名临时表；
                    MariaDB [hellodb]> EXPLAIN SELECT Name FROM students UNION SELECT Name FROM teachers;
                    +------+--------------+------------+-------+---------------+------+---------+------+------+-------------+
                    | id   | select_type  | table      | type  | possible_keys | key  | key_len | ref  | rows | Extra       |
                    +------+--------------+------------+-------+---------------+------+---------+------+------+-------------+
                    |    1 | PRIMARY      | students   | index | NULL          | name | 152     | NULL |   27 | Using index |
                    |    2 | UNION        | teachers   | ALL   | NULL          | NULL | NULL    | NULL |    4 |             |
                    | NULL | UNION RESULT | <union1,2> | ALL   | NULL          | NULL | NULL    | NULL | NULL |             |
                    +------+--------------+------------+-------+---------------+------+---------+------+------+-------------+
                    3 rows in set (0.00 sec)

        table：SELECT 语句关联到的表；

        type：关联类型，或访问类型，即MySQL决定的如何去查询表中的行的方式；
            ALL：全表扫描；
            index：根据索引的次序进行全表扫描；如果在Extra列出现"Using index"表示了使用覆盖索引，而非全表扫描；
            range：有范围限制的根据索引实现范围扫描；扫描位置始于索引中的某一点，结束于另一点；
            ref：根据索引返回表中匹配某单个值的所有行；
            eq_ref：仅返回一个行，但需要额外与某个参考值做比较；
            const, system：直接返回单个行；
            NULL：MySQL在优化阶段分解查询语句，在执行阶段无需再访问表或索引；

        possible_keys：查询可能会用到的索引；

        key：查询中使用了的索引；

        key_len：在索引中使用的字节数；

        ref：在利用key字段所表示的索引完成查询时所用的列或某常量值；

        rows：MySQL估计为找到所有的目标行而需要读取的行数；

        Extra：额外信息
            Using index：MySQL将会使用覆盖索引，以避免访问表；
            Using where：MySQL服务器将在存储引擎检索后，再进行一次过滤；
            Using temporary：MySQL对结果排序时会使用临时表；
            Using filesort：MySQL对结果使用一个外部索引排序；

```

(完)
