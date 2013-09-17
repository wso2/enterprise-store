/*
 Description: Initialization script
 Filename:app.js
 Created Date: 29/7/2013
 */

var caramel = require('caramel'),
    rxt_management = require('/modules/rxt.manager.js').rxt_management(),
    route_management = require('/modules/router-g.js').router(),
    config = require('/config/publisher.json'),
    carbon = require('carbon'),
    mediaType = 'application/vnd.wso2.registry-ext-type+xml',
    conf = carbon.server.loadConfig('carbon.xml'),
    offset = conf.*::['Ports'].*::['Offset'].text(),
    hostName = conf.*::['HostName'].text().toString();

var ext_parser = require('/modules/ext/core/extension.parser.js').extension_parser();
var ext_domain = require('/modules/ext/core/extension.domain.js').extension_domain();
var ext_core = require('/modules/ext/core/extension.core.js').extension_core();
var ext_mng = require('/modules/ext/core/extension.management.js').extension_management();

if (hostName === null || hostName === '') {
    hostName = 'localhost';
}

var httpPort = 9763 + parseInt(offset, 10);
var httpsPort = 9443 + parseInt(offset, 10);


var process = require('process');
process.setProperty('server.host', hostName);
process.setProperty('http.port', httpPort.toString());
process.setProperty('https.port', httpsPort.toString());

var pubConfig = require('/config/publisher.js').config();
var server = require('/modules/server.js');
server.init(pubConfig);

var user = require('/modules/user.js');
user.init(pubConfig);

var publisher = require('/modules/publisher.js');
publisher.init(pubConfig);




//var SUPER_TENANT_ID=-1234;
//var event = require('/modules/event.js');
//event.emit('tenantLoad', SUPER_TENANT_ID);

//Configure Caramel
caramel.configs({
    context: '/publisher',
    cache: true,
    negotiation: true,
    themer: function () {
        //TODO: Hardcoded theme
        return 'default';
    }

});


//Cause the super tenant to be load
var SUPER_TENANT = -1234;
var event = require('/modules/event.js');
event.emit('tenantLoad', SUPER_TENANT);



