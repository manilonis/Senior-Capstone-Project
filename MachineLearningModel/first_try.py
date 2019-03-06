import numpy as np
import tensorflow as tf
from tensorflow import keras


def main():
    print("Hello World")
    fake_input = np.random.rand(3, 6)
    fake_output = np.random.rand(3, 1)
    print(fake_input)
    print(fake_output)

    model = keras.Sequential()
    model.add(keras.layers.Dense(10, activation=keras.activations.relu, input_shape=(6, )))
    model.add(keras.layers.Dense(10, activation=keras.activations.softmax))

    model.compile(optimizer='adam',
                  loss='sparse_categorical_crossentropy',
                  metrics=['accuracy'])
    model.fit(fake_input, fake_output, epochs=3, verbose=3)

    print(np.argmax(model.predict(fake_input)[0]))


if __name__ == "__main__":
    main()
