import react from "@vitejs/plugin-react";
import { defineConfig } from "vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    https: false,
    proxy: {
      "/api": {
        target: "http://localhost:7071",
        changeOrigin: true,
        // secure: false,
        // rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
});
