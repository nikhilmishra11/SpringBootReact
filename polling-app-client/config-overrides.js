const { injectBabelPlugin } = require('react-app-rewired');
const rewireLess = require('react-app-rewire-less');

module.exports = function override(config, env) {
    config = injectBabelPlugin(['import', { libraryName: 'antd', style: true }], config);
    config = rewireLess.withLoaderOptions({
        modifyVars: {
            "@layout-body-background": "#FFFFFF",
            "@layout-header-background": "#FFFFFF",
            "@layout-footer-background": "#FFFFFF"
        },
        module: {
            rules: [
                {
                    test: /\.js$/,
                    exclude: /node_modules/,
                    use: {loader: "babel-loader"}
                },
                {
                    test: /\.html$/,
                    use: [
                        {loader: "html-loader"}
                    ]
                }
            ]
        },
        javascriptEnabled: true
    })(config, env);
    return config;
};