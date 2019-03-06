import requests
import sys
import random

if __name__ == "__main__":
    resp = requests.get('http://pi.cs.oswego.edu:4567/2004')
    if resp.status_code != 200:
        print("Something went wrong")
        sys.exit(5)
    json_response = resp.json()
    countries = json_response["countries"]
    new_resp = requests.get('http://pi.cs.oswego.edu:4567/2004/' + countries[random.randint(0, len(countries))])
    new_json = new_resp.json()
    print(new_resp.status_code)
    keys = list(new_json["data"].keys())
    print(keys)
    print(new_json["data"][keys[random.randint(0, len(keys))]])
    # print(len(json_response["countries"]))
    # print(resp.json())
