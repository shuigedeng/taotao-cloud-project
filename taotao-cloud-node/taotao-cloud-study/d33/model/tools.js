const multer = require('multer');
const path = require('path');
let tools={
    multer(){

        var storage = multer.diskStorage({
            //配置上传的目录
            destination: function (req, file, cb) {
                cb(null, 'static/upload') //上传之前目录必须存在
            },
            //修改上传后的文件名
            filename: function (req, file, cb) {
                //1、获取后缀名
                let extname= path.extname(file.originalname);
                //2、根据时间戳生成文件名
                cb(null, Date.now()+extname)
            }
        })
        
        var upload = multer({ storage: storage })

        return upload;
        
    },
    md5(){

    }
}

module.exports=tools