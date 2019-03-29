from MachineLearningModel.large_data_pull import data_grab
import numpy as np
import copy
from tensorflow import keras

def arrays():
    print("Go for data grab")
    data = data_grab(None)
    keys = list(data.keys())
    keys_specific = list(data[keys[0]].keys())
    list_array = []
    length_list = []
    for k in keys:
        temp = []
        temp_length = None
        c_data = data[k]
        for ks in keys_specific:
            if ks == 'Number of years':
                temp_length = c_data[ks]
                continue
            item = c_data[ks]
            temp.append(item)
        if -1 in temp:
            continue
        list_array.append(copy.deepcopy(temp))
        length_list.append(copy.deepcopy(temp_length))
    print(len(list_array))
    data_array = np.array(list_array, dtype=np.float)
    print(data_array.shape)
    power_array = np.array(length_list, dtype=np.int)
    print(power_array.shape)

    return data_array, power_array


def get_dist(out_array: np.ndarray):
    out_array = out_array.tolist()
    dist = {}
    for o in out_array:
        if o in dist:
            dist[o] = dist[o] + 1
        else:
            dist[o] = 1
    print(dist)


def crete_model(in_array, out_array):
    print("Start Machine Learning")
    model = keras.Sequential()
    model.add(keras.layers.Dense(10, activation='relu', input_shape=(5,)))
    model.add(keras.layers.Dense(10, activation='relu'))
    model.add(keras.layers.Dense(6, activation=keras.activations.softmax))
    model.compile(optimizer='adam',
                  loss=keras.losses.categorical_crossentropy,
                  metrics=['accuracy'])
    get_dist(out_array)
    out_array = keras.utils.to_categorical(out_array)
    print(out_array.shape)
    model.fit(in_array, out_array, epochs=100, verbose=0, batch_size=5)
    pred = model.predict(in_array)
    print(pred.shape)
    results = [np.argmax(y) for y in pred]
    print(results)
    print(model.evaluate(x=in_array, y=out_array))
    #print(model.predict(in_array))



if __name__ == '__main__':
    i_array, o_array = arrays()
    crete_model(i_array, o_array)
