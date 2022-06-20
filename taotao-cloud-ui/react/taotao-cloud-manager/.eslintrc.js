module.exports = {
  parser: '@typescript-eslint/parser',
  settings: {
    'import/resolver': {
      alias: {
        map: [['@', './src']],
        extensions: ['.ts', '.tsx', '.js', '.jsx', '.json'],
      },
    },
  },
  extends: [
    'react-app',
    'plugin:prettier/recommended',
    'plugin:@typescript-eslint/recommended',
    'prettier/@typescript-eslint',
    'prettier/babel',
    'prettier/react',
  ],
  plugins: [
    '@typescript-eslint',
    'import',
    'jsx-a11y',
    'prettier',
    'react',
    'react-hooks',
  ],
  rules: {
    'prettier/prettier': 2,
    "no-empty-function": "off",
    "no-useless-escape": "off",
    "@typescript-eslint/no-empty-function": ["off", { "allow": ["functions"] }],
    'import/no-anonymous-default-export': [2, {allowArray: true}],
    '@typescript-eslint/no-var-requires': 0,
    '@typescript-eslint/no-non-null-assertion': 0,
    '@typescript-eslint/no-use-before-define': 2,
    '@typescript-eslint/explicit-module-boundary-types': 0,
    "@typescript-eslint/consistent-type-assertions": "off",
    '@typescript-eslint/consistent-type-imports': "off",
    '@typescript-eslint/ban-tslint-comment': 'off',
    '@typescript-eslint/ban-ts-comment': 'off',
    '@typescript-eslint/no-explicit-any': 'off',
    '@typescript-eslint/ban-types': 'off'
  },
}
