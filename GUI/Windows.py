import tkinter
from tkinter import IntVar, Label, Radiobutton, StringVar, Entry, Button
from tkinter.ttk import Combobox
from tensorflow import keras
import copy
import requests
import os
import numpy as np
import random


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
                print()
        if resp is None or resp.status_code != 200:
            json_text = ["test", "Test", "Testy"]
        else:
            print(resp)
            json_text = resp.json()['countries']
        Label(self, text="Countries Available for selected year (" + str(button.cget('text')) + "):")\
            .grid(row=4, column=0, columnspan=4, sticky='W')
        if isinstance(json_text, list):
            self.selected_country.set(json_text[0])
            self.countries['values'] = json_text
            self.countries.current(1)
        else:
            self.selected_country.set("test")
            self.countries['values'] = json_text
            self.countries.current(1)
        self.countries.grid(row=5, column=0, columnspan=6, sticky='W')
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
        Label(self, text="Predict years of existence", font='Helvetica 12 bold').grid(row=0, column=0, sticky='EW', columnspan=4)

        # First group of labels and entries
        Label(self, text="Military Budget Growth\n Rate").grid(row=1, column=0, sticky='W')
        self.military_budget = StringVar()
        Entry(self, textvariable=self.military_budget).grid(row=2, column=0)

        Label(self, text="Average Unemployment\n Rate").grid(row=1, column=1)
        self.unemployment = StringVar()
        Entry(self, textvariable=self.unemployment).grid(row=2, column=1)

        Label(self, text="Average GDP growth\n rate").grid(row=1, column=2)
        self.gdp = StringVar()
        Entry(self, textvariable=self.gdp).grid(row=2, column=2)

        Label(self, text="Average Labor Force").grid(row=1, column=3)
        self.labor_force = StringVar()
        Entry(self, textvariable=self.labor_force).grid(row=2, column=3)

        # Second group of labels and entries
        Label(self, text="Average Population").grid(row=3, column=0)
        self.population = StringVar()
        Entry(self, textvariable=self.population).grid(row=4, column=0)

        Label(self, text="Average Import\n to Export Ratio").grid(row=3, column=1)
        self.i_to_e_ratio = StringVar()
        Entry(self, textvariable=self.i_to_e_ratio).grid(row=4, column=1)

        Label(self, text="Average Budget").grid(row=3, column=2)
        self.budget = StringVar()
        Entry(self, textvariable=self.budget).grid(row=4, column=2)

        Label(self, text="Average External Debt").grid(row=3, column=3)
        self.ext_debt = StringVar()
        Entry(self, textvariable=self.ext_debt).grid(row=4, column=3)

        Button(self, text="Predict!", command=self.predict).grid(row=5, column=0, columnspan=4)

        self.model_keys = {1: [1, 3, 4, 5], 2: [0, 2, 6, 7], 3: [0, 1, 3, 5], 4: [2, 4, 6, 7], 5: [0, 2, 5, 6],
                           6: [1, 3, 4, 7], 7: [0, 5, 6, 7], 8: [1, 2, 3, 4]}
        self.current_data = []

    def predict(self):
        c_widgets = self.winfo_children()
        for w in c_widgets:
            if (w.grid_info()['row'] == 6 or w.grid_info()['row'] == 7) and isinstance(w, Label):
                w.destroy()
        Label(self, text="Years this country would exist:").grid(row=6, column=0, sticky='W', columnspan=2)

        m_num = self.get_model_number()
        if m_num < 0:
            print('ERROR')
            self.current_data.clear()
            Label(self, text='ERROR').grid(row=7, column=0)
            return
        model = None
        for root, dirs, files in os.walk("../MLStuff"):
            for file in files:
                file_name = str(file)
                if ".txt" in file_name or ".pickle" in file_name:
                    continue
                else:
                    if 'model'+str(m_num - 1) in file_name:
                        model = keras.models.load_model('../MLStuff/' + file_name)
                    elif m_num == 1 and 'model.h5' == file_name:
                        model = keras.models.load_model('../MLStuff/' + file_name)

        if model is None:
            print('ERROR')
            self.current_data.clear()
            Label(self, text='ERROR').grid(row=7, column=0)
            return

        float_data = list(map(int, self.current_data))
        float_data = np.asarray(float_data, dtype=np.float32)
        if model.layers[0].input_shape[1] == 5:
            temp = list(float_data.tolist())
            temp.append(float(random.randint(1, 14)))
            float_data = np.asarray(temp)

        prediction = np.argmax(model.predict(np.array([float_data, ])))
        Label(self, text=str(prediction)).grid(row=7, column=0)
        self.current_data.clear()

    def get_model_number(self):
        selected = []
        model = -1
        if len(self.military_budget.get()) > 0:
            selected.append(0)
            self.current_data.append(self.military_budget.get())
        if len(self.unemployment.get()) > 0:
            selected.append(1)
            self.current_data.append(self.unemployment.get())
        if len(self.gdp.get()) > 0:
            selected.append(2)
            self.current_data.append(self.gdp.get())
        if len(self.labor_force.get()) > 0:
            selected.append(3)
            self.current_data.append(self.labor_force.get())
        if len(self.population.get()) > 0:
            selected.append(4)
            self.current_data.append(self.population.get())
        if len(self.i_to_e_ratio.get()) > 0:
            selected.append(5)
            self.current_data.append(self.i_to_e_ratio.get())
        if len(self.budget.get()) > 0:
            selected.append(6)
            self.current_data.append(self.budget.get())
        if len(self.ext_debt.get()) > 0:
            selected.append(7)
            self.current_data.append(self.ext_debt.get())

        if len(selected) > 4:
            return model
        for k in list(range(1, 9)):
            list.sort(selected)
            if selected == self.model_keys[k]:
                model = k
                break
        return model
