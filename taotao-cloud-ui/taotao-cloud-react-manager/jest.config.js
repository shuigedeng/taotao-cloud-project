const path = require('path')

module.exports = {
  rootDir: path.resolve(__dirname),
  clearMocks: true,
  coverageDirectory: 'coverage',
  coverageProvider: 'v8',
  setupFiles: ['<rootDir>/jest.setup.ts'],
  moduleFileExtensions: ['js', 'json', 'jsx', 'ts', 'tsx', 'node'],
  moduleNameMapper: {
    '@/(.*)$': '<rootDir>/src/components/$1'
  },
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  testMatch: ['<rootDir>/test/*.spec.ts?(x)'],
  transform: {
    '^.+\\js$': 'babel-jest',
    '^.+\\.(t|j)sx?$': 'ts-jest'
  },
  transformIgnorePatterns: ['\\\\node_modules\\\\'],
  snapshotSerializers: ['enzyme-to-json/serializer']
}
