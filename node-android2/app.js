/**
 * Module dependencies.
KarthikIntern
 */
var express  = require('express');
var connect = require('connect');
var app      = express();
var port     = process.env.PORT || 8082;
// Configuration
app.use(express.static(__dirname + '/public'));
app.use(connect.logger('dev'));
app.use(connect.json());
app.use(connect.urlencoded());
// Routes
require('./routes/routes.js')(app);
app.listen(port);
console.log('NodeApp runs on port ' + port);
