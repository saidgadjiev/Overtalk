var as = angular.module('OverTalkApp', ['ngRoute', 'ui.bootstrap', 'OverTalkApp.controllers']);

as.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'html/login.html',
            controller: 'LoginController'
        })
        .when('/main', {
            templateUrl: 'html/main.html',
            publicAccess: true
        })
        .when('/login', {
            templateUrl: 'html/login.html',
            controller: 'LoginController'
        })
        .when('/posts', {
            templateUrl: 'html/posts/posts.html',
            controller: 'PostsController'
        }).when('/posts/new', {
            templateUrl: 'html/posts/new.html',
            controller: 'NewPostController'
        })
        .when('/posts/:id', {
            templateUrl: 'html/posts/details.html',
            controller: 'DetailsController'
        });
});
