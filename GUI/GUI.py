import tkinter
from tkinter import Label, Button
from Windows import APIWindow, MachineLearningWindow


def run():
    Label(master, text="Machine Learning the CIA World Factbook").grid(row=0, column=0, columnspan=2)
    b = Button(master, text="Try the API", command=api_window)
    b.grid(row=1, column=0)
    bm = Button(master, text="Predict Years of Existence", command=ml_window)
    bm.grid(row=1, column=1)
    master.mainloop()


def api_window():
    APIWindow(master)


def ml_window():
    MachineLearningWindow(master)


if __name__ == "__main__":
    master = tkinter.Tk()
    value = tkinter.IntVar()
    run()
