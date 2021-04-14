const fs = require('fs');

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


    return new Promise((resolve, reject) => {
        fs.readFile('./data/mime.json', (err, data) => {
            if (err) {
                console.log(err);
                reject(err);
                return;
            }
            let mimeObj = JSON.parse(data.toString());
            // console.log(mimeObj[extname]);
            resolve(mimeObj[extname]);

        })
    })

}



