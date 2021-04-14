/*
1、https://www.npmjs.com/package/mkdirp

2、cnpm i mkdirp --save  /  npm i mkdirp --save 

3、var mkdirp = require('mkdirp');

4、看文档使用

*/

var mkdirp = require('mkdirp');

// mkdirp('./upload', function (err) {
//     if (err) {
//         console.error(err);
//     }    
// });

// mkdirp('./uploadDir');

mkdirp('./upload/aaa/xxxx', function (err) {
    if (err) {
        console.error(err);
    }
});
