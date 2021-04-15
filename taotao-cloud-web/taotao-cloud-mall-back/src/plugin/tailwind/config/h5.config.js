module.exports = {
  purge: {
    enabled: true,
    content: ['./src/**/*.{js,jsx,ts,tsx,vue}'],
    defaultExtractor: content => content.match(/[\w-/:]+(?<!:)/g) || [],
  },
};
