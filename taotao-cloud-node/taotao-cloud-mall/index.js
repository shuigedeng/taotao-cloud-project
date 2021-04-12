const express = require('express');
const app = express();
const port = 3000;

app.get('/', function(req, res) {
    res.send('Hello World!')
});

app.listen(port, function(){
    console.log(`请求访问127.0.0.1:${port}!`)
});
