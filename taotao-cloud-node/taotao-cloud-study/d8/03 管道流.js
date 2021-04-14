// 管道流

// const fs=require('fs');
// var readStream=fs.createReadStream('./aaa.jpg');

// var writeStream=fs.createWriteStream('./data/aaa.jpg');

// readStream.pipe(writeStream);



const fs=require('fs');
var readStream=fs.createReadStream('./demo08.zip');

var writeStream=fs.createWriteStream('./data/demo.zip');

readStream.pipe(writeStream);