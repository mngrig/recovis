const express = require('express');
const https = require('https');
const fs = require('fs');
const path = require('path');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

// Get files from build directory
app.use(express.static(path.join(__dirname, 'build')));

// Proxy middleware
app.use('/', createProxyMiddleware({
    target: 'http://localhost:40060',
    changeOrigin: true,
}));

// GET requests
app.get('*', function(req, res) {
    res.sendFile(path.join(__dirname, 'build', 'index.html'));
});

// HTTP server
const httpServer = app.listen(40060, function() {
    console.log('HTTP server running on port 40060');
});

/*
// HTTPS server
const options = {
    key: fs.readFileSync('/path/to/your/private.key'),
    cert: fs.readFileSync('/path/to/your/certificate.crt')
};

const httpsServer = https.createServer(options, app);
httpsServer.listen(40061, function() {
    console.log('HTTPS server running on port 40061');
});
*/