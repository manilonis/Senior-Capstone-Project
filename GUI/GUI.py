import tkinter
from tkinter import Label, Button, Toplevel, Radiobutton
from API_Window import API_Window


def run():
    Label(master, text="Machine Learning the CIA World Factbook").grid(row=0, column=0, columnspan=2)
    b = Button(master, text="Try the API", command=api_window)
    b.grid(row=1, column=0)
    bm = Button(master, text="Predict Years of Existence")
    bm.grid(row=1, column=1)
    master.mainloop()


def api_window():
    top = API_Window(master)
    # Label(top, text="Api Tester").grid(row=0, column=0, columnspan=7)
    #
    # Label(top, text="Please select a year:").grid(row=1, column=0)
    # years = ['2011', '2012', '2007', '2008', '2004', '2002', '2006', '2010', '2009', '2003', '2000', '2005',
    #          '2013', '2014']
    # radio_buttons = []
    # count = 0
    # for year in years:
    #     rb = Radiobutton(top, text=year, variable=value, value=count)
    #     if count < 7:
    #         rb.grid(row=2, column=count+1)
    #     else:
    #         rb.grid(row=3, column=(count+1)-7)
    #     radio_buttons.append(copy.copy(rb))
    #     count += 1
    # print(count)


if __name__ == "__main__":
    master = tkinter.Tk()
    value = tkinter.IntVar()
    run()
