let role = 'customer'

var url = document.location.pathname;
var sp = url.split('/');
role = sp[sp.length - 1]

function getApi(apiPath) {
    return '/portal/api' + apiPath
}

function getPage(pagePath) {
    return '/portal/pages' + pagePath
}

function backToMain() {
    var main = '/portal/'
    if (role != null && role != 'customer') {
        main += 'perspective/' + role
    }
    window.location = main
}

function buildHttpHead() {
    var head = new Headers();
    head.append("Content-Type", "application/json");
    return head;
}
