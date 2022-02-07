module.exports = {
  plugins: ['stylelint-prettier', 'stylelint-order'],
  extends: [
    'stylelint-config-standard',
    'stylelint-prettier/recommended',
    'stylelint-config-rational-order',
  ],
  rules: {
    'prettier/prettier': [true, { singleQuote: true }],
    'at-rule-no-unknown': [
      true,
      {
        ignoreAtRules: [
          'mixin',
          'include',
          'extend',
          'each',
          'function',
          'return',
        ],
      },
    ],
  },
  ignoreFiles: ['build', 'public'],
}
