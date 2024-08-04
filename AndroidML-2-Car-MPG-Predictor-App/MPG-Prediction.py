# -*- coding: utf-8 -*-
"""
Created on Tue Apr 20 09:46:57 2021

@author: Soorya Parthiban
"""

##-----------------------------------------------------------------------------
## Importing the Required Libraries

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns

import tensorflow as tf

from tensorflow import keras
from tensorflow.keras import layers
from tensorflow.keras.layers.experimental import preprocessing

print(tf.__version__)

##-----------------------------------------------------------------------------
## Importing the Dataset

auto_df = pd.read_csv(r"D:\001_Data\Regression Android\MPG Prediction\auto-mpg.csv")

auto_df.head()
auto_df.tail()
auto_df.info()
auto_df.describe().transpose()
auto_df.isnull().sum()

auto_df.shape
auto_df.columns

##-----------------------------------------------------------------------------
## Data Visualization

# PLOT 1

sns.pairplot(auto_df[['mpg', 'cylinders', 'displacement', 'horsepower', 'weight',
       'acceleration']], diag_kind='kde')

# PLOT 2

plt.figure(figsize=(10,5))
plt.hist(auto_df["mpg"], bins=50)
plt.title("Distribution of MPG")
plt.xlabel("Fuel Efficiency")
plt.ylabel("Count")

# PLOT 3

plt.figure(figsize=(10,5))
plt.boxplot(auto_df["mpg"])
plt.title("Boxplot of MPG")
plt.xlabel("Fuel Efficiency")
plt.ylabel("Count")

# PLOT 4

plt.figure(figsize=(10,5))
sns.boxplot(data=auto_df, x="cylinders", y="mpg")
plt.title("Relationship between MPG and Number of Cylinders")
plt.xlabel("Number of Cylinders")
plt.ylabel("Fuel Efficiency")

# PLOT 5

plt.figure(figsize=(10,5))
sns.scatterplot(data=auto_df, x="displacement", y="mpg")
plt.title("Relationship between MPG and Displacement of the Vehicle")
plt.xlabel("Displacement")
plt.ylabel("Fuel Efficiency")

# PLOT 6

plt.figure(figsize=(10,5))
sns.scatterplot(data=auto_df, x="horsepower", y="mpg")
plt.title("Relationship between MPG and Horsepower of the Vehicle")
plt.xlabel("Horsepower")
plt.ylabel("Fuel Efficiency")

# PLOT 7

plt.figure(figsize=(10,5))
sns.scatterplot(data=auto_df, x="weight", y="mpg")
plt.title("Relationship between MPG and Weight Of the Vehicle")
plt.xlabel("Weight")
plt.ylabel("Fuel Efficiency")

# PLOT 8

plt.figure(figsize=(10,5))
sns.scatterplot(data=auto_df, x="acceleration", y="mpg")
plt.title("Relationship between MPG and Acceleration Of the Vehicle")
plt.xlabel("Accelaration")
plt.ylabel("Fuel Efficiency")

# PLOT 9

plt.figure(figsize=(15,5))
sns.boxplot(data=auto_df, x="model year", y="mpg")
plt.title("Relationship between MPG and Year Of the Vehicle")
plt.xlabel("Vehicle's Year")
plt.ylabel("Fuel Efficiency")

#PLOT 10

auto_df['Vehicle Country'] = auto_df['origin'].map({1: 'USA', 2: 'Europe', 3: 'Japan'})

plt.figure(figsize=(10,5))
sns.boxplot(data=auto_df, x="Vehicle Country", y="mpg")
plt.title("Relationship between MPG and Manufactured Country Of the Vehicle")
plt.xlabel("Manufactured Country")
plt.ylabel("Fuel Efficiency")

##-----------------------------------------------------------------------------
## Data Pre-Processing & Partitioning

country_dummies = pd.get_dummies(auto_df["Vehicle Country"], prefix="Country",
                                 prefix_sep="-")

print(country_dummies[0:5])

auto_df = pd.concat([auto_df, country_dummies], axis=1)

auto_df.columns

X = auto_df[['cylinders', 'displacement', 'horsepower', 'weight',
       'acceleration', 'model year', 'Country-Europe', 'Country-Japan',
       'Country-USA']]

y = auto_df['mpg']


from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3)

from sklearn.preprocessing import MinMaxScaler
scaler = MinMaxScaler()

X_train = scaler.fit_transform(X_train)
X_test = scaler.fit_transform(X_test)


print(X_train.shape, X_test.shape)

##-----------------------------------------------------------------------------
## Building DL Models

## Plot used to visualize the Training & Validation Loss

def plot_loss(history):
  plt.figure(figsize=(10,5))  
  plt.plot(history.history['loss'], label='loss')
  plt.plot(history.history['val_loss'], label='val_loss')
  plt.ylim([0, 10])
  plt.xlabel('Epoch')
  plt.ylabel('Error')
  plt.legend()
  plt.grid(True)


## MODEL 1
###########  

model_1 = tf.keras.models.Sequential([
    tf.keras.layers.Dense(256, activation="relu", input_shape=(9,)),
    tf.keras.layers.Dense(128, activation="relu"),
    tf.keras.layers.Dense(64, activation="relu"),
    tf.keras.layers.Dense(1)
])

model_1.compile(loss='mean_squared_error', optimizer='adam')

model_1.summary()

history1 = model_1.fit(X_train, y_train, epochs=500, validation_split = 0.2)

model1_history = pd.DataFrame(history1.history)
model1_history['No.Of Epoch'] = history1.epoch   

model1_history.head()
model1_history.tail()

plot_loss(history1)

y_preds_1 = model_1.predict(X_test)

a = plt.axes(aspect='equal')
plt.scatter(y_test, y_preds_1)
plt.title("Model 1 (Adam) Performance")
plt.xlabel('True Values [MPG]')
plt.ylabel('Predictions [MPG]')
lims = [0, 50]
plt.xlim(lims)
plt.ylim(lims)
_ = plt.plot(lims, lims)

## MODEL 2
###########

model_2 = tf.keras.models.Sequential([
    tf.keras.layers.Dense(256, activation="relu", input_shape=(9,)),
    tf.keras.layers.Dense(128, activation="relu"),
    tf.keras.layers.Dense(64, activation="relu"),
    tf.keras.layers.Dense(1)
])

model_2.compile(loss='mean_squared_error', optimizer='adamax')

model_2.summary()

history2 = model_2.fit(X_train, y_train, epochs=500, validation_split = 0.2)

model2_history = pd.DataFrame(history2.history)
model2_history['No.Of Epoch'] = history2.epoch   

model2_history.head()
model2_history.tail()

plot_loss(history2)

y_preds_2 = model_2.predict(X_test)

a = plt.axes(aspect='equal')
plt.scatter(y_test, y_preds_2)
plt.title("Model 2 (Adamax) Performance")
plt.xlabel('True Values [MPG]')
plt.ylabel('Predictions [MPG]')
lims = [0, 50]
plt.xlim(lims)
plt.ylim(lims)
_ = plt.plot(lims, lims)


## MODEL 3
###########

model_3 = tf.keras.models.Sequential([
    tf.keras.layers.Dense(256, activation="relu", input_shape=(9,)),
    tf.keras.layers.Dense(128, activation="relu"),
    tf.keras.layers.Dense(64, activation="relu"),
    tf.keras.layers.Dense(1)
])

model_3.compile(loss='mean_squared_error', optimizer='RMSprop')

model_3.summary()

history3 = model_3.fit(X_train, y_train, epochs=500, validation_split = 0.2)

model3_history = pd.DataFrame(history3.history)
model3_history['No.Of Epoch'] = history3.epoch   

model3_history.head()
model3_history.tail()

plot_loss(history3)

y_preds_3 = model_3.predict(X_test)

a = plt.axes(aspect='equal')
plt.scatter(y_test, y_preds_3)
plt.title("Model 3 (Adamax) Performance")
plt.xlabel('True Values [MPG]')
plt.ylabel('Predictions [MPG]')
lims = [0, 50]
plt.xlim(lims)
plt.ylim(lims)
_ = plt.plot(lims, lims)

##-----------------------------------------------------------------------------

from sklearn.metrics import r2_score

r2_model1 = r2_score(y_test, y_preds_1)
r2_model2 = r2_score(y_test, y_preds_2)
r2_model3 = r2_score(y_test, y_preds_3)

Model_Evaluations = {}

Model_Evaluations["Adam"] = r2_model1
Model_Evaluations["Adamax"] = r2_model2
Model_Evaluations["RMSProp"] = r2_model3

results_items = Model_Evaluations.items()
result_list = list(results_items)

df = pd.DataFrame(result_list)

r2score_df = df.rename(columns={0: 'ModelName', 1: 'R2Score'}) 
r2score_df.head()

plt.figure(figsize=(10,7))
plt.title("Comparing the Model's R2 Score")
plt.bar(r2score_df['ModelName'], r2score_df['R2Score'])
plt.xlabel("Model with respective Optimizers")
plt.xlabel("R2 Score of the Model")

print(r2score_df)

#   ModelName   R2Score
# 0      Adam  0.878702
# 1    Adamax  0.863110
# 2   RMSProp  0.881594

##-----------------------------------------------------------------------------
## Saving the best Model

Keras_file = "AutoMPG-Regressor.h5"

tf.keras.models.save_model(model_3, Keras_file)
converter = tf.lite.TFLiteConverter.from_keras_model(model_3)
tfmodel = converter.convert()
open("AutoMPG-Regressor.tflite","wb").write(tfmodel)