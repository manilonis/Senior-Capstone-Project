from MachineLearningModel.large_data_pull import data_grab
import numpy as np
import copy
from typing import Union
from tensorflow import keras


def arrays():
    print("Go for data grab")
    data = data_grab(['2011', '2012', '2007', '2008', '2004', '2002', '2006', '2010', '2009', '2003', '2000', '2005'])
    keys = list(data.keys())
    keys_specific = list(data[keys[0]].keys())
    list_array = []
    length_list = []
    short_country = {}
    for k in keys:
        temp = []
        temp_length = None
        c_data = data[k]
        for ks in keys_specific:
            if ks == 'Number of years':
                temp_length = c_data[ks]
                if temp_length < 5:
                    short_country[k] = False
                continue
            item = c_data[ks]
            temp.append(item)
        # if -1 in temp:
        #     if k in short_country:
        #         short_country[k] = True
        #     continue
        list_array.append(copy.deepcopy(temp))
        length_list.append(copy.deepcopy(temp_length))
    print(len(list_array))
    data_array = np.array(list_array, dtype=np.float)
    print(data_array.shape)
    power_array = np.array(length_list, dtype=np.int)
    print(power_array.shape)

    return data_array, power_array


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


def crete_model(in_array, out_array):
    print(in_array.shape)
    print(out_array.shape)
    print("Start Machine Learning")
    model = keras.Sequential()
    model.add(keras.layers.Dense(10, activation='relu', input_shape=(5,)))
    model.add(keras.layers.Dense(10, activation='relu'))
    get_dist(out_array)
    out_array = keras.utils.to_categorical(out_array)
    model.add(keras.layers.Dense(out_array.shape[1], activation=keras.activations.softmax))
    model.compile(optimizer='adam',
                  loss=keras.losses.categorical_crossentropy,
                  metrics=['accuracy'])

    print(out_array.shape)
    model.fit(in_array, out_array, epochs=600, verbose=0, batch_size=2)
    pred = model.predict(in_array)
    print(pred.shape)
    results = [np.argmax(y) for y in pred]
    print(results)
    print(get_dist(results))
    print(model.evaluate(x=in_array, y=out_array))

    for i in range(len(results)):
        if results[i] < 5:
            print(in_array[i])


if __name__ == '__main__':
    i_array, o_array = arrays()
    crete_model(i_array, o_array)
