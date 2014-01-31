var assetLinks = function (user) {

    return{
        title: 'API',
        links: [
            {
                title: 'Custom',
                url: 'custom',
                path: 'custom.jag'
            } ,
            {
                title: 'My Applications',
                url: 'myapps',
                path: 'myapps.jag'
            },
            {
                title: 'Login',
                url: 'login',
                path: 'login.jag'
            },
            {
                url: 'logout',
                path: 'logout.jag'
            },
            {
                 title:'Custom',
                 url:'custom',
                 path:'custom.jag'
            } ,
            {
                title:'Subscriptions',
                url:'subscriptions',
                path:'subscriptions.jag'
            }
        ]
    }
};

var assetManager = function (manager) {
    var add = manager.add;
    var log = new Log('asset');

    log.info('Custom asset manager loaded');

    //Override the add actions of the API
    manager.add = function (options) {
        add.call(manager, options);
    };

    return manager;
};