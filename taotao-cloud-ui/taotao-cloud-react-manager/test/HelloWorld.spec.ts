import {expect} from "@jest/globals";

const add = (a: number, b: number): number => {
  return a + b
}

describe('add(1,2)', () => {
  it('等于3', () => {
    expect(add(1, 2)).toEqual(3)
  })
})