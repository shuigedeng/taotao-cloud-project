

# tpl = "i am {name}, age {age}, really {name}".format(name="seven", age=18)
#
# tpl = "i am {name}, age {age}, really {name}".format(**{"name": "seven", "age": 18})

# tpl = "i am {:s}, age {:d}".format(*["seven", 18])
# tpl = "i am {:s}, age {:d}".format("seven", 18) #["seven", 18]
#
# l=["seven", 18]
# tpl = "i am {:s}, age {:d}".format('seven',18)
# print(tpl)

tpl = "numbers: {:b},{:o},{:d},{:x},{:X}, {:%},{}".format(15, 15, 15, 15, 15, 15.87623, 2)
print(tpl)