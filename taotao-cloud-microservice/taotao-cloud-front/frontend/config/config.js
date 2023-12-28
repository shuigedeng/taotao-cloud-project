const fs = require('fs');

fs.copyFile('src/main/webapp/index.html', 'build/www/index.html', (err) => {
    if (err) {
        throw err;
    }
    console.log('Copied index.html!');
});
