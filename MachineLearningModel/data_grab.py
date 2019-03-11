import requests


def main():
    country = 'Canada'
    years_available = ['2004', '2002', '2006', '2003', '2005']
    json_responses = []
    for year in years_available:
        resp = requests.get('http://pi.cs.oswego.edu:4567/'+year+'/'+country)
        json_responses.append(resp.json())

    for j in json_responses:
        print(j["data"]["Constitution:"])


if __name__ == "__main__":
    main()
