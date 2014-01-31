var resource = (function () {
    var log = new Log('application-api');

    var addApplication = function (context) {

        log.info('Adding an application');
        var parameters = context.request.getAllParameters();

        for (var key in parameters) {
            log.info(key + ' ' + parameters[key]);
        }

        var AppService = require('/extensions/assets/api/services/app.js').serviceModule;

        appApi = new AppService.AppService();

        appApi.init(jagg, session);

        var result = appApi.addApplication({
            username: 'admin',
            application: parameters.appName,
            tier: parameters.appTier,
            callbackUrl: parameters.appCallbackUrl,
            description: parameters.appDescription
        });

        return result;
    };

    var deleteApplication = function (context) {
        log.info('Application delete');
        var AppService = require('/extensions/assets/api/services/app.js').serviceModule;
        appApi = new AppService.AppService();
        appApi.init(jagg, session);


        var uriMatcher = new URIMatcher(context.request.getRequestURI());
        var URI = '/{context}/resources/{asset}/{version}/application/{appName}';

        log.info(request.getRequestURI());

        var isMatch=uriMatcher.match(URI);
        log.info(stringify(uriMatcher.elements()));


        if(isMatch){
            var appName=uriMatcher.elements().appName;
            log.info('Removing '+appName);
            appApi.deleteApplication({
                appName:appName,
                username:'admin'
            });

            return {isRemoved:true};
        }

        //var isRemoved=appApi.deleteApplication()

        return {isRemoved: false};
    };

    var updateApplication = function (context) {

        log.info('Entered update application');

        var parameters = request.getContent();

        var AppService = require('/extensions/assets/api/services/app.js').serviceModule;

        appApi = new AppService.AppService();

        appApi.init(jagg, session);

        log.info(parameters);

        var result=appApi.updateApplication({
            newAppName:parameters.newAppName,
            oldAppName:parameters.appName,
            username:'admin',
            tier:parameters.tier,
            callbackUrl:parameters.newCallbackUrl,
            description:parameters.newDescription
        });

        return result;
    };

    return{
        post: addApplication,
        delete: deleteApplication,
        put:updateApplication
    }

})();
