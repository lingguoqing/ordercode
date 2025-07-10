# Vue3 打字机流式SSE前端示例

## 运行方式

1. 进入本目录：
   ```sh
   cd frontend-vue3-sse-typer
   ```
2. 安装依赖：
   ```sh
   npm install
   ```
3. 启动开发服务器：
   ```sh
   npm run dev
   ```
4. 默认接口地址为 `/ai/stream`，如需跨域请配置代理。

## 主要文件
- `src/App.vue`：主页面，打字机流式效果演示
- `vite.config.js`：如需代理后端接口可在此配置 