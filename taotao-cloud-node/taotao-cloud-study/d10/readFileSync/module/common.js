const fs=require('fs');

exports.getMime = function (extname) {

    switch (extname) {
        case '.html':
            return 'text/html';
        case '.css':
            return 'text/css';
        case '.js':
            return 'text/javascript';
        default:
            return 'text/html';

    }

}

exports.getFileMime = function (extname) {

    var data=fs.readFileSync('./data/mime.json'); //同步方法
    let mimeObj=JSON.parse(data.toString());   
    return mimeObj[extname];    

}



