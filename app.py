#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Jan 30 17:09:30 2020

@author: colosu
"""

import torch
import torch.nn as nn
from flask import Flask, jsonify, request

class Net(nn.Module):

	def __init__(self):
		super(Net, self).__init__()
		self.inp = nn.Linear(6, 12)
		self.lkr1 = nn.LeakyReLU()
		self.hid = nn.Linear(12, 3)
		self.lkr2 = nn.LeakyReLU()
		self.out = nn.Linear(3, 1)

	def forward(self, x):
		x = self.inp(x)
		x = self.lkr1(x)
		x = self.hid(x)
		x = self.lkr2(x)
		x = self.out(x)
		return x

#Start server
app = Flask(__name__)

#Paths to models
PPATH = './PearsonANN.pth'
SPATH = './SpearmanANN.pth'

#Load models
pearson = Net()
pearson.load_state_dict(torch.load(PPATH))
pearson.eval()
spearman = Net()
spearman.load_state_dict(torch.load(SPATH))
spearman.eval()

#Def functions
def transform_data(data):
	return torch.FloatTensor(data)

def get_prediction(data, model):
    tensor = transform_data(data)
    outputs = model.forward(tensor)
#    _, predicted = outputs.max(1)
    return outputs

@app.route('/predict', methods=['POST'])
def predict():
	if request.method == 'POST':
		# we will get the file from the request
		data = request.json['data']
		alphaP = get_prediction(data, pearson)
		alphaS = get_prediction(data, spearman)
		print(alphaP)
		return jsonify({'pearson_alpha': str(alphaP.detach().numpy()[0]), 'spearman_alpha':str(alphaS.detach().numpy()[0])})

if __name__ == '__main__':
    app.run()
