import requests
import pprint


def main():
    country = 'Canada'
    years_available = ['2004', '2002', '2006', '2003', '2005']
    json_responses = []
    for year in years_available:
        resp = requests.get('http://pi.cs.oswego.edu:4567/'+year+'/'+country)
        json_responses.append(resp.json()["data"])

    count = 0
    for j in json_responses:
        if count == 0:
            pp = pprint.PrettyPrinter(indent=1)
            pp.pprint(sorted(list(j.keys())))
            # print(list(j.keys()))
            count += 1
        print(j["Airports:"])


def main2():
    country = 'Canada'
    years_available = ['2004', '2002', '2006', '2003', '2005']
    json_responses = []
    for year in years_available:
        resp = requests.get('http://pi.cs.oswego.edu:4567/' + year + '/' + country)
        json_responses.append(resp.json()["data"])

    key_count = {}
    for j in json_responses:
        key_list = list(j.keys())
        for k in key_list:
            if k in key_count:
                c = key_count[k]
                key_count[k] = c+1
            else:
                key_count[k] = 1

    fives = []
    for key in key_count.keys():
        if key_count[key] == 5:
            fives.append(key)
    print(key_count)
    pp = pprint.PrettyPrinter(indent=1)
    pp.pprint(sorted(fives))
    print(len(fives))
    # print(fives)

def countires():
    years_available = ['2004', '2002', '2006', '2003', '2005']
    json_responses = []
    for y in years_available:
        resp = requests.get('http://pi.cs.oswego.edu:4567/' + y)
        json_responses.append(resp.json()['countries'])
        print(resp.json())

    list_diff = {}
    for j in json_responses:
        for c in j:
            if c in list_diff:
                a = list_diff[c]
                list_diff[c] = a + 1
            else:
                list_diff[c] = 1

    for k in list_diff.keys():
        if list_diff[k] < 5:
            print(list_diff[k])
            print(k)
    print(list_diff)


if __name__ == "__main__":
    # main()
    main2()
    countires()
