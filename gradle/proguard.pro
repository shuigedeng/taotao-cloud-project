#指定Java的版本
-target 25
#proguard会对代码进行优化压缩，他会删除从未使用的类或者类成员变量等
-dontshrink
#是否关闭字节码级别的优化，如果不开启则设置如下配置
-dontoptimize
#混淆时不生成大小写混合的类名，默认是可以大小写混合
-dontusemixedcaseclassnames
# 对于类成员的命名的混淆采取唯一策略
-useuniqueclassmembernames
#混淆时不生成大小写混合的类名，默认是可以大小写混合
-dontusemixedcaseclassnames
#混淆类名之后，对使用Class.forName('className')之类的地方进行相应替代
-adaptclassstrings

#对异常、注解信息予以保留
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
# 此选项将保存接口中的所有原始名称（不混淆）-->
-keepnames interface ** { *; }
# 此选项将保存所有软件包中的所有原始接口文件（不进行混淆）
#-keep interface * extends * { *; }
#保留参数名，因为控制器，或者Mybatis等接口的参数如果混淆会导致无法接受参数，xml文件找不到参数
-keepparameternames
# 保留枚举成员及方法
-keepclassmembers enum * { *; }
# 不混淆所有类,保存原始定义的注释-
-keepclassmembers class * {
                        @org.springframework.context.annotation.Bean *;
                        @org.springframework.beans.factory.annotation.Autowired *;
                        @org.springframework.beans.factory.annotation.Value *;
                        @org.springframework.stereotype.Service *;
                        @javax.persistence.Table *;
                        @javax.persistence.Entity *;
                        }


-keepclasseswithmembers public class * { public static void main(java.lang.String[]);} ##保留main方法的类及其方法名
-keep public class ch.qos.logback.**{*;}
-keep class com.fasterxml.jackson.** { *; }
-keep public class com.fasterxml.jackson.** { *; }

#忽略warn消息
-ignorewarnings
#忽略note消息
-dontnote
#打印配置信息
-printconfiguration
