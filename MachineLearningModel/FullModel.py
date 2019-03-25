import requests
import time


def data_grab(years):
    year_json_response = []
    cdict = {}
    for year in years:
        print("Go year " + str(year))
        resp = requests.get('http://pi.cs.oswego.edu:4567/' + year, timeout=10)
        c = resp.json()['countries']
        year_json_response.append(c)
        for ctries in c:
            # print("Go for " + str(ctries))
            resp = requests.get('http://pi.cs.oswego.edu:4567/' + year + '/' + ctries, timeout=10)
            if resp.status_code != 200:
                print("BAD CODE" + str(resp.status_code) + " ERROR")
                break
            if ctries in cdict:
                cdict[ctries].append(resp.json())
                print("Found " + str(ctries))
            else:
                print("Did not find. Created " + str(ctries))
                cdict[ctries] = [resp.json()]
            time.sleep(0.5)

    print(len(cdict))

    return


if __name__ == "__main__":
    # Stop Key: K1qJI9FZtMMZtvq5FnFc
    data_grab(['2004', '2002', '2006', '2003', '2005'])
