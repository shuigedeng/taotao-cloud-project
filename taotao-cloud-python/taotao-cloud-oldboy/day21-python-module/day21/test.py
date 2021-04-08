#
# tag=True
# while tag:
#     print('leve1')
#     choice=input("level1>>: ").strip()
#     if choice == 'quit':break
#     if choice == 'quit_all': tag = False
#     while tag:
#         print('level2')
#         choice = input("level2>>: ").strip()
#         if choice == 'quit': break
#         if choice == 'quit_all': tag = False
#         while tag:
#             print('level3')
#             choice = input("level3>: ").strip()
#             if choice == 'quit': break
#             if choice == 'quit_all': tag = False

# import os
# # os.rename('a.txt','a.txt.bak')
# os.rename('b.txyt','a.txt')

import re
import sys
print(sys.path)
print(re.findall(r"123","1234214"))  #['123']

