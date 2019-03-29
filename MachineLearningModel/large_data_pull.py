import requests
import re
from typing import Union
import pickle
import os


def data_grab(years: Union[list, None]) -> dict:
    all_data = {}
    file_there = os.path.isfile('./data.pickle')
    if file_there:
        with open('./data.pickle', 'rb') as f:
            all_data = pickle.load(f)
        return all_data
    if years is None:
        years = ['2004', '2002', '2006', '2003', '2005']
    year_json_response = []
    cdict = {}
    for year in years:
        print("Go year " + str(year))
        resp = requests.get('http://pi.cs.oswego.edu:4567/' + year, timeout=10)
        c = resp.json()['countries']
        year_json_response.append(c)
        for ctries in c:
            resp = requests.get('http://pi.cs.oswego.edu:4567/' + year + '/' + ctries, timeout=10)
            if resp.status_code != 200:
                print("BAD CODE" + str(resp.status_code) + " ERROR")
                break
            if ctries in cdict:
                cdict[ctries].append(resp.json())
                # print("Found " + str(ctries))
            else:
                # print("Did not find. Created " + str(ctries))
                cdict[ctries] = [resp.json()]

    print(len(cdict))
    keys = list(cdict.keys())
    print(cdict['Lithuania'][0]['data']['Unemployment rate:'])
    print(cdict['Canada'][0]['data']['Unemployment rate:'])

    for k in keys:
        all_data[k] = {'Number of years': len(cdict[k])}
        all_data[k].update(grab_average_growth_rate(cdict, k, 'GDP - real growth rate:'))
        all_data[k].update(grab_average_budget_change(cdict, k))
        all_data[k].update(grab_average_growth_rate(cdict, k, 'Military expenditures - percent of GDP:'))
        all_data[k].update(get_average_import_export_ratio(cdict, k))
        all_data[k].update(grab_average_growth_rate(cdict, k, 'Unemployment rate:'))

    with open('./data.pickle', 'wb') as f:
        pickle.dump(all_data, f)
    return all_data


def grab_average_growth_rate(cnt_list: dict, key: str, data_key: str) -> dict:
    numbers = []
    if 'Military' in data_key:
        method_data_key = 'Average Military budget growth rate'
        r = {'Average Military budget growth rate': -1}
    elif 'Unemploy' in data_key:
        method_data_key = 'Average Unemployment Rate'
        r = {'Average Unemployment Rate': -1}
    else:
        method_data_key = 'Average GDP growth rate'
        r = {'Average GDP growth rate': -1}

    for i in range(len(cnt_list[key])):
        d = cnt_list[key][i]
        try:
            percent = d['data'][data_key]
        except KeyError:
            break
        if 'NA' in percent or '$' in percent:
            break
        rexp = re.search("-\d", percent)
        if rexp:
            n_index = rexp.start()
        else:
            rexp = re.search("\d", percent)
            if not rexp:
                break
            n_index = rexp.start()

        index = percent.find('%')
        try:
            numbers.append(float(percent[n_index:index]))
        except ValueError:
            break
        if i == len(cnt_list[key]) - 1:
            r[method_data_key] = (sum(numbers) / float(len(cnt_list[key])))
    return r


def grab_average_budget_change(cnt_list: dict, key: str) -> dict:
    numbers = []
    r = {'Average Budget': -1}
    for i in range(len(cnt_list[key])):
        d = cnt_list[key][i]
        try:
            budget_string = d['data']['Budget:']
        except KeyError:
            break
        exp_index = budget_string.find('expenditures:')
        inc_index = budget_string.find('including')
        budget_string = budget_string[exp_index:inc_index]
        start_re = re.search('\$', budget_string)
        if start_re:
            start_re = start_re.start() + 1
        else:
            break
        end_re, multiple = get_multiple(budget_string[start_re:])
        # print(budget_string[start_re:])
        end_re = end_re + (len(budget_string) - len(budget_string[start_re:]))
        if multiple == 0:
            break

        num = float(budget_string[start_re:end_re])
        num *= multiple
        numbers.append(num)
        if len(numbers) == len(cnt_list[key]):
            r['Average Budget'] = sum(numbers) / float(len(numbers))
    return r


def get_average_import_export_ratio(cnt_list: dict, key: str) -> dict:
    numbers = []
    r = {'Average Import to Export Ratio': -1}
    for i in range(len(cnt_list[key])):
        d = cnt_list[key][i]
        try:
            import_string = d['data']['Imports:']
            export_string = d['data']['Exports:']
        except KeyError:
            break
        if 'NA' in import_string or 'NA' in export_string:
            break
        start_i = import_string.find("$") + 1
        if start_i <= 0:
            start_i = 1
        start_e = export_string.find("$") + 1
        if start_e <= 0:
            start_e = 1
        multiples = [0, 0]
        i_end, multiples[0] = get_multiple(import_string)
        e_end, multiples[1] = get_multiple(export_string)
        if 0 in multiples:
            break
        try:
            imports = float(import_string[start_i:(i_end-1)].strip())
            imports *= multiples[0]
            exports = float(export_string[start_e:(e_end-1)].strip())
            exports *= multiples[1]
            numbers.append(float(imports/exports))
        except ValueError as v:
            break
    if len(numbers) > 0:
        r['Average Import to Export Ratio'] = sum(numbers) / float(len(numbers))
    return r


def get_multiple(s: str) -> (int, int):
    multiple = 0
    m = s.find('illion')
    sub = s[m-2:m].strip()
    if sub == 'b':
        multiple = 10**9
    elif sub == 'm':
        multiple = 10**6
    elif sub == 'tr':
        multiple = 10**12
    return (m-len(sub)), multiple


if __name__ == "__main__":
    # Stop Key: K1qJI9FZtMMZtvq5FnFc
    data_grab(['2004', '2002', '2006', '2003', '2005'])
