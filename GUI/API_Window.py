import tkinter
from tkinter import IntVar, Label, Radiobutton, Text, END
import copy
import requests


class API_Window(tkinter.Toplevel):

    def __init__(self, master=None):
        tkinter.Toplevel.__init__(self, master=master)
        self.radio_var = IntVar()
        self.text_box = None
        Label(self, text="Api Tester").grid(row=0, column=0, columnspan=7)

        Label(self, text="Please select a year:").grid(row=1, column=0, columnspan=3)
        years = ['2011', '2012', '2007', '2008', '2004', '2002', '2006', '2010', '2009', '2003', '2000', '2005',
                 '2013', '2014']
        list.sort(years)
        self.radio_buttons = []
        count = 0
        for year in years:
            rb = Radiobutton(self, text=year, variable=self.radio_var, value=count, command=self.radio_button_changed)
            if count < 7:
                rb.grid(row=2, column=count + 1)
            else:
                rb.grid(row=3, column=(count + 1) - 7)
            self.radio_buttons.append(copy.copy(rb))
            count += 1

    def radio_button_changed(self):
        button = self.radio_buttons[self.radio_var.get()]
        print(button.cget('text'))
        resp = requests.get('http://pi.cs.oswego.edu:4567/' + str(button.cget('text')), timeout=10)
        Label(self, text="Countries Available for selected year (" + str(button.cget('text')) + "):").grid(row=4, column=0, columnspan=4)
        self.text_box = Text(self, height=5)
        self.text_box.grid(row=5, column=0, columnspan=8)
        self.text_box.insert(END, resp.json()['countries'])
        print('Request got')
