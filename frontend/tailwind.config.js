/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        nubank: {
          50: '#faf5ff',
          100: '#f3e8ff',
          500: '#8a05be',
          600: '#7400a8',
          700: '#5f008f',
          900: '#2f004f'
        },
        ink: '#1f1a24'
      },
      boxShadow: {
        soft: '0 18px 45px rgba(30, 0, 60, 0.10)'
      }
    }
  },
  plugins: []
}
