const plugin = require('tailwindcss/plugin')

module.exports = {
  purge: ['./src/**/*.{js,jsx,ts,tsx}', './index.html'],
  darkMode: false, // or 'media' or 'class'
  theme: {
    screens: {
      'sm': '640px',
      'md': '768px',
      'lg': '1024px',
      'xl': '1280px',
      '2xl': '1536px',
    },
    backgroundColor: theme => ({
      ...theme('colors'),
      f4f7f9: '#f4f7f9',
      secondary: '#ffed4a',
      danger: '#e3342f'
    }),
    fontFamily: {
      display: ['Gilroy', 'sans-serif'],
      body: ['Graphik', 'sans-serif']
    },
    borderWidth: {
      'none': '0',
      'sm': '.125rem',
      DEFAULT: '.25rem',
      'lg': '.5rem',
      'full': '9999px',
    },
    extend: {
      colors: {
        cyan: '#9cdbff'
      },
      spacing: {
        '13': '3.25rem',
        '15': '3.75rem',
        '96': '24rem',
        '128': '32rem'
      }
    }
  },
  variants: {
    appearance: ['responsive'],
    backgroundColor: ['responsive', 'hover', 'focus'],
    fill: [],
    extend: {
      flex: ['hover', 'focus'],
      flexDirection: ['hover', 'focus'],
      flexWrap: ['hover', 'focus'],
      flexGrow: ['hover', 'focus'],
      flexShrink: ['hover', 'focus'],
      order: ['hover', 'focus'],
      boxSizing: ['hover', 'focus'],
      display: ['hover', 'focus'],
      float: ['hover', 'focus'],
      clear: ['hover', 'focus'],
      objectFit: ['hover', 'focus'],
      objectPosition: ['hover', 'focus'],
      overflow: ['hover', 'focus'],
      overscrollBehavior: ['hover', 'focus'],
      position: ['hover', 'focus'],
      inset: ['hover', 'focus'],
      visibility: ['hover', 'focus'],
      zIndex: ['hover', 'active'],
      justifyContent: ['hover', 'focus'],
    }
  },
  plugins: [
    require('tailwindcss-transforms'),
    require('tailwindcss-transitions'),
    require('tailwindcss-border-gradients'),
    plugin(function ({ addUtilities, addComponents, e, prefix, config }) {
      // Add your custom styles here

    })
  ]
}
