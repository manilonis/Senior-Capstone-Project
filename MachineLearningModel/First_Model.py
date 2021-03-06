try:
    from MachineLearningModel.large_data_pull import data_grab, data_grab_two
except ImportError:
    from large_data_pull import data_grab, data_grab_two
try:
    import h5py
except ImportError:
    print('Models may not be able to save or load')
import numpy as np
import copy
from typing import Union
import random
from tensorflow import keras
from sklearn.metrics import confusion_matrix, f1_score
import pickle
import os


def arrays():
    print("Go for data grab")
    years = ['2011', '2012', '2007', '2008', '2004', '2002', '2006', '2010', '2009', '2003', '2000', '2005',
             '2013', '2014']
    data_1 = data_grab(years)
    data_2 = data_grab_two(years)

    data = {}
    for k in list(data_1.keys()):
        new_temp = {}
        new_temp.update(data_1[k])
        new_temp.update(data_2[k])
        data[k] = copy.deepcopy(new_temp)
    keys = list(data.keys())
    l_1, l_2 = spilt_list_randomly(list(data[keys[random.randint(0, len(keys)-1)]].keys()))
    keys_specific = list(data[keys[0]].keys())
    list_array = []
    list_array_for_second_model = []
    length_list = []
    for k in keys:
        temp = []
        temp_2 = []
        temp_length = None
        c_data = data[k]
        for ks in keys_specific:
            if ks == 'Number of years':
                temp_length = c_data[ks]
            if ks in l_2:
                item = c_data[ks]
                temp_2.append(item)
            else:
                item = c_data[ks]
                temp.append(item)
        list_array.append(copy.deepcopy(temp))
        list_array_for_second_model.append(copy.deepcopy(temp_2))
        length_list.append(copy.deepcopy(temp_length))
    print(len(list_array))
    data_array = np.array(list_array, dtype=np.float)
    print(data_array.shape)
    power_array = np.array(length_list, dtype=np.int)
    print(power_array.shape)

    second_input_array = np.array(list_array_for_second_model, dtype=np.float)
    print(second_input_array.shape)

    return data_array, power_array, second_input_array, l_1, l_2


def spilt_list_randomly(key_list: list):
    key_list = copy.deepcopy(key_list).remove('Number of years')
    list_1 = []
    list_2 = []
    numbers_hit = []
    list_len_1 = int(len(key_list) / 2)
    list_len_2 = len(key_list) - list_len_1
    while len(list_1) < list_len_1 or len(list_2) < list_len_2:
        r = random.randint(0, len(key_list)-1)
        while r in numbers_hit:
            r = random.randint(0, len(key_list)-1)
        numbers_hit.append(r)
        r_1 = random.randint(0, 100)
        if len(list_1) == list_len_1:
            list_2.append(key_list[r])
        elif len(list_2) == list_len_2:
            list_1.append(key_list[r])
        else:
            if r_1 % 2 == 1:
                list_1.append(key_list[r])
            else:
                list_2.append(key_list[r])
    return list_1, list_2


def get_dist(out_array: Union[np.ndarray, list]):
    if isinstance(out_array, np.ndarray):
        out_array = copy.deepcopy(out_array).tolist()
    dist = {}
    for o in out_array:
        if o in dist:
            dist[o] = dist[o] + 1
        else:
            dist[o] = 1
    print(dist)


def get_dist_ratio(out_array: Union[np.ndarray, list]):
    if isinstance(out_array, np.ndarray):
        out_array = copy.deepcopy(out_array).tolist()
    dist = {}
    for o in out_array:
        if o in dist:
            dist[o] = dist[o] + 1
        else:
            dist[o] = 1
    keys = list(dist.keys())
    ratio_dist = {}
    length = len(out_array)
    for k in keys:
        ratio_dist[k] = float(float(dist[k]) / float(length))

    return ratio_dist


def check_ratios(original_dist: dict, results_dist: dict, difference: float):
    org_keys = list(original_dist.keys())
    r_keys = list(results_dist.keys())
    if set(org_keys) != set(r_keys):
        print('One item is missing')
        print(set(set(org_keys) - set(r_keys)))
        return False
    for k in org_keys:
        diff = abs(original_dist[k] - results_dist[k])
        if diff > difference:
            print(k + " is bad")
            return False
    return True


def crete_model(in_array: np.ndarray, out_array: np.ndarray, run_till_good_dist: bool = False, max_loops: int = 25,
                verbose: int = 0):
    print(in_array.shape)
    print(out_array.shape)
    # print("Start Machine Learning")
    model = keras.Sequential()
    model.add(keras.layers.Dense(10, activation='relu', input_shape=(int(in_array.shape[1]),)))
    model.add(keras.layers.Dense(10, activation='relu'))
    get_dist(out_array)
    org_ratios = get_dist_ratio(out_array)
    out_array = keras.utils.to_categorical(out_array)
    model.add(keras.layers.Dense(out_array.shape[1], activation=keras.activations.softmax))
    model.compile(optimizer='adam',
                  loss=keras.losses.categorical_crossentropy,
                  metrics=['accuracy'])

    print(out_array.shape)
    good_ratio = False
    if not run_till_good_dist:
        max_loops = 1
    loop_count = 0
    while loop_count < max_loops and not good_ratio:
        model.fit(in_array, out_array, epochs=500, verbose=verbose, batch_size=2)
        pred = model.predict(in_array)
        print(pred.shape)
        results = [np.argmax(y) for y in pred]
        print(results)
        print(get_dist(results))

        new_ratios = get_dist_ratio(results)
        print(new_ratios)
        good_ratio = check_ratios(org_ratios, new_ratios, 0.01)
        print(good_ratio)
        loop_count += 1
        print('Loop is at: ' + str(loop_count))

    # confusion = confusion_matrix(out_array.argmax(axis=1), results)
    # print(confusion)

    print(model.evaluate(x=in_array, y=out_array))
    return model, results


def write_model_evals(model: keras.models.Sequential, params: list, x_array: np.ndarray, y_array: np.ndarray,
                      predictions: list = None):
    if len(y_array.shape) < 2:
        y_tarray = keras.utils.to_categorical(copy.deepcopy(y_array))
    else:
        y_tarray = copy.deepcopy(y_array)
    print(x_array.shape, y_tarray.shape)
    with open('model_results.txt', 'a') as f:
        s = 'Model with parameters: ' + str(params)
        if predictions is None:
            s += '\n' + "Has accuracy: " + str(model.evaluate(x=x_array, y=y_tarray)[1]) + '\n'
        else:
            s += '\n' + "Has accuracy: " + str(model.evaluate(x=x_array, y=y_tarray)[1]) + ' Has f1: '\
                 + str(f1_score(y_array, predictions, average='micro')) + '\n'
        f.write(s)
        f.close()


def keys_check(key_1: list, key_2: list = None):
    if keys_2 is None:
        if not os.path.isfile('./keys.pickle'):
            return False
        with open('./keys.pickle', 'rb') as f:
            list_of_list = pickle.load(f)
            for l in list_of_list:
                if set(keys_1) == set(l):
                    f.close()
                    return True
        f.close()
        return False
    else:
        exist = os.path.isfile('./keys.pickle')
        list_of_list = []
        if exist:
            with open('./keys.pickle', 'rb') as f:
                list_of_list = pickle.load(f)
                f.close()
        list_of_list.append(keys_1)
        list_of_list.append(keys_2)
        with open('./keys.pickle', 'wb') as f:
            pickle.dump(list_of_list, f)
            f.close()


def save_model(model):
    exist = os.path.isfile('./model.h5')
    if exist:
        keep_going = True
        count = 1
        while keep_going:
            exist = os.path.isfile('./model' + str(count) + '.h5')
            if not exist:
                model.save('./model' + str(count) + '.h5')
                keep_going = False
            else:
                count += 1
    else:
        model.save('./model.h5')


if __name__ == '__main__':
    i_array, o_array, i_2_array, keys_1, keys_2 = arrays()
    if keys_check(keys_1) and keys_check(keys_2):
        i_array, o_array, i_2_array, keys_1, keys_2 = arrays()
        if keys_check(keys_1) and keys_check(keys_2):
            print("Exiting as can not get random parameters for the model. Please run again")
            print("If the problem persists you may have run through all the combinations")
            exit(2)
    m_1, pred = crete_model(i_array, copy.deepcopy(o_array), run_till_good_dist=True)
    write_model_evals(m_1, keys_1, i_array, o_array, predictions=pred)
    save_model(m_1)
    print('Go second model')
    m_2, pred = crete_model(i_2_array, copy.deepcopy(o_array), run_till_good_dist=True)
    write_model_evals(m_2, keys_2, i_2_array, o_array, predictions=pred)
    save_model(m_2)
