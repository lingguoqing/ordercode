const path = require('path');

module.exports = {
  webpack: {
    configure: (webpackConfig, { env, paths }) => {
      // Set the path to the new public folder
      paths.appPublic = path.resolve(__dirname, 'src/public');
      // Set the path to the new index.html
      paths.appHtml = path.resolve(__dirname, 'src/public/index.html');
      
      // Update the HtmlWebpackPlugin to use the new template path
      webpackConfig.plugins.forEach(plugin => {
        if (plugin.constructor.name === 'HtmlWebpackPlugin') {
          plugin.options.template = path.resolve(__dirname, 'src/public/index.html');
        }
      });

      return webpackConfig;
    }
  },
  devServer: {
    static: {
        directory: path.join(__dirname, 'src/public'),
      },
  }
}; 