var storage = window.localStorage;

var roleList = ['customer', 'supplier', 'manufacturer', 'distributor', 'router', 'deliver'];

var url = document.location.pathname;
var sp = url.split('/');
var temp = sp[sp.length - 1];

var role = null;

if (roleList.includes(temp)) {
    role = temp;
}

if (role != null) {
    storage.role = role;
}

function getApi(apiPath) {
    return '/portal/api' + apiPath
}

function getPage(pagePath) {
    return '/portal/pages' + pagePath
}

function backToMain() {
    var main = '/portal/'
    // if (role != null && role != 'customer') {
    //     main += 'perspective/' + role
    // }
    window.location = main
}

function buildHttpHead() {
    var head = new Headers();
    head.append("Content-Type", "application/json");

    if (storage.sessionKey != null) {
        head.append("customSession", storage.sessionKey)
    }

    return head;
}
