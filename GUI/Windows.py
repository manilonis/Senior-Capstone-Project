import tkinter
from tkinter import IntVar, Label, Radiobutton, StringVar, Entry, Button
from tkinter.ttk import Combobox
import copy
import requests


class APIWindow(tkinter.Toplevel):

    def __init__(self, master=None):
        tkinter.Toplevel.__init__(self, master=master)
        self.radio_var = IntVar()
        self.text_box = None
        Label(self, text="Api Tester").grid(row=0, column=0, columnspan=7, sticky='EW')

        Label(self, text="Please select a year:").grid(row=1, column=0, columnspan=3, sticky='W')
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
        self.selected_country = StringVar()
        self.countries = Combobox(self, values=None)
        self.countries.bind("<<ComboboxSelected>>", self.combo_box_event)
        self.country_options = Combobox(self, values=None)
        self.country_options.bind("<<ComboboxSelected>>", self.country_data_selected)

    def radio_button_changed(self):
        button = self.radio_buttons[self.radio_var.get()]
        print(button.cget('text'))
        resp = None
        try:
            resp = requests.get('http://pi.cs.oswego.edu:4567/' + str(button.cget('text')), timeout=2)
        except requests.exceptions.ConnectionError:
            json_text = ["test", "Test", "TEsty"]
        if resp is None or resp.status_code != 200:
            json_text = ["test", "Test", "TEsty"]
        else:
            print(resp)
            json_text = resp.json()['countries']
        Label(self, text="Countries Available for selected year (" + str(button.cget('text')) + "):").grid(row=4, column=0, columnspan=4, sticky='W')
        if isinstance(json_text, list):
            self.selected_country.set(json_text[0])
            self.countries['values'] = json_text
            self.countries.current(1)
        else:
            self.selected_country.set("test")
            self.countries['values'] = json_text
            self.countries.current(1)
        self.countries.grid(row=5, column=0, columnspan=6, sticky='W')
        # self.countries.set_menu(*json_text)
        print('Request got')

    def combo_box_event(self, event):
        country = str(self.countries.get())
        year = self.radio_buttons[self.radio_var.get()].cget('text')
        resp = requests.get('http://pi.cs.oswego.edu:4567/' + year + '/' + country, timeout=10)
        data = resp.json()['data']
        Label(self, text="Data Keys for this country:").grid(row=6, column=0, columnspan=3, sticky='W')
        Label(self, text="Data of selected key:").grid(row=6, column=4, columnspan=3, sticky='W')
        self.country_options['values'] = list(data.keys())
        self.country_options.grid(row=7, column=0, sticky='W')

    def country_data_selected(self, event):
        c_widgets = self.winfo_children()
        for w in c_widgets:
            r = w.grid_info()['row']
            if r == 7 and isinstance(w, Label):
                w.destroy()
        country = str(self.countries.get())
        year = self.radio_buttons[self.radio_var.get()].cget('text')
        resp = requests.get('http://pi.cs.oswego.edu:4567/' + year + '/' + country, timeout=10)
        data = resp.json()['data']
        Label(self, text=str(data[self.country_options.get()])).grid(row=7, column=4, sticky='W')


class MachineLearningWindow(tkinter.Toplevel):
    def __init__(self, master=None):
        tkinter.Toplevel.__init__(self, master=master)
        Label(self, text="Predict years of existence").grid(row=0, column=0, sticky='EW', columnspan=4)
        Label(self, text="Military Budget\n Growth Rate").grid(row=1, column=0, sticky='W')
        Entry(self).grid(row=2, column=0)
        Label(self, text="Average Unemployment\n Rate").grid(row=1, column=1)
        Entry(self).grid(row=2, column=1)
        Label(self, text="Average GDP growth\n rate").grid(row=1, column=2)
        Entry(self).grid(row=2, column=2)
        Label(self, text="Average Labor Force").grid(row=1, column=3)
        Entry(self).grid(row=2, column=3)
        Button(self, text="Predict!", command=self.predict).grid(row=3, column=0, columnspan=4)

    def predict(self):
        print('predict')
