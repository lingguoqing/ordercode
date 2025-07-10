import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  server: {
    // 如需代理后端接口，取消下方注释并配置
    proxy: {
      '/ai': 'http://localhost:9636',
    },
  },
}); 