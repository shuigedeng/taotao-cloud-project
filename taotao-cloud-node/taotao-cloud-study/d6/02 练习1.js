// 判断服务器上面有没有upload目录。如果没有创建这个目录，如果有的话不做操作。   （图片上传）

const fs = require('fs');
var path = './upload';

fs.stat(path, (err, data) => {
    if (err) {
        //执行创建目录
        mkdir(path);
        return;
    }
    if (!data.isDirectory()) {
        //首先删除文件，再去执行创建目录
        fs.unlink(path, (err) => {
            if (!err) {
                mkdir(path);
            } else {
                console.log('请检测传入的数据是否正确');
            }
        })
    }
})

//创建目录的方法
function mkdir(dir) {
    fs.mkdir(dir, (err) => {
        if (err) {
            console.log(err);
            return;
        }
    });
}
