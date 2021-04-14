/*
 1. fs.stat   检测是文件还是目录
 2. fs.mkdir  创建目录
 3. fs.writeFile  创建写入文件
 4. fs.appendFile 追加文件
 5. fs.readFile 读取文件
 6. fs.readdir读取目录
 7. fs.rename 重命名 移动文件
 8. fs.rmdir  删除目录
 9. fs.unlink 删除文件

*/

const fs=require('fs');

// 1. fs.stat   检测是文件还是目录

    /*
        fs.stat('./html',(err,data)=>{
            if(err){
                console.log(err);
                return;
            }

            console.log(`是文件:${data.isFile()}`);
            console.log(`是目录:${data.isDirectory()}`);

        })
    */


    /*
        fs.stat('./package.json',(err,data)=>{
            if(err){
                console.log(err);
                return;
            }

            console.log(`是文件:${data.isFile()}`);
            console.log(`是目录:${data.isDirectory()}`);

        })
    */



//2、fs.mkdir  创建目录

    /*
    path            将创建的目录路径
    mode            目录权限（读写权限），默认777
    callback        回调，传递异常参数err
    */

    /*
        fs.mkdir('./css',(err)=>{

            if(err){
                console.log(err);
                return;
            }
            console.log('创建成功');
        })
    */



// 3. fs.writeFile  创建写入文件

    /*
        filename      (String)            文件名称
        data        (String | Buffer)    将要写入的内容，可以使字符串 或 buffer数据。
        options        (Object)           option数组对象，包含：
        · encoding   (string)            可选值，默认 ‘utf8′，当data使buffer时，该值应该为 ignored。
        · mode         (Number)        文件读写权限，默认值 438
        · flag            (String)            默认值 ‘w'
        callback {Function}  回调，传递一个异常参数err。
    */


    /*
        fs.writeFile('./html/index.html','你好nodejs',(err)=>{

            if(err){
                console.log(err);
                return;
            }
            console.log('创建写入文件成功');
        })



    */


    /*
    fs.writeFile('./html/index.html','你好nodejs 哈哈',(err)=>{

            if(err){
                console.log(err);
                return;
            }
            console.log('创建写入文件成功');
        })
    */



// 4. fs.appendFile 追加文件

    /*
        fs.appendFile('./css/base.css','body{color:red}',(err)=>{

            if(err){
                console.log(err);
                return;
            }
            console.log('appendFile 成功');

        })

    */ 

    /*
        fs.appendFile('./css/base.css','h3{color:red}\n',(err)=>{

            if(err){
                console.log(err);
                return;
            }
            console.log('appendFile 成功');

        })


    */



// 5.fs.readFile 读取文件

    /*

        fs.readFile('./html/index.html',(err,data)=>{
            if(err){
                console.log(err);
                return;
            }
            console.log(data);
            console.log(data.toString());  //把Buffer 转化成string类型
        })
    */


    // no such file or directory, open 'D:\node_demo\demo05\aaa\index.html
    /*
    fs.readFile('./aaa/index.html',(err,data)=>{
            if(err){
                console.log(err);
                return;
            }
            console.log(data);
            console.log(data.toString());  //把Buffer 转化成string类型
        })
    */



//6.fs.readdir读取目录   [ 'index.html', 'js', 'news.html' ]

    /*
        fs.readdir('./html',(err,data)=>{

            if(err){
                console.log(err);
                return;
            }

            console.log(data);
        })
    */


// 7.fs.rename 重命名   功能:1、表示重命名 2、移动文件

  /*
      fs.rename('./css/aaa.css','./css/index.css',(err)=>{

        if(err){
            console.log(err);
            return;
        }

        console.log('重命名成功');

    })
  */


    /*
    fs.rename('./css/index.css','./html/index.css',(err)=>{

        if(err){
            console.log(err);
            return;
        }
        console.log('移动文件成功');

    })
    */




//  8. fs.rmdir  删除目录

    fs.rmdir('./aaaa',(err)=>{
        if(err){
            console.log(err);
            return;
        }
        console.log('删除目录成功');
    })




//9. fs.unlink 删除文件

    /*
        fs.unlink('./aaaa/index.html',(err)=>{
            if(err){
                console.log(err);
                return;
            }
            console.log('删除文件成功');
        })
    */