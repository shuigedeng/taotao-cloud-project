import requests


ret = requests.post(
    url='http://dig.chouti.com/link/vote?linksId=11825081',
    cookies={
        'gpsd': 'c9c202cc9fe82779c2bf87b34809019b'
    }
)
print(ret.text)