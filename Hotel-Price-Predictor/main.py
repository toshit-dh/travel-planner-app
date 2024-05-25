import streamlit as st
import pickle
import numpy as np
import pandas as pd


st.title('TravelPlanner Hotel Price Predictor')
df=pd.read_csv('hotel-dataset.csv')
df.drop_duplicates(inplace=True)

model = pickle.load(open('rfpipe.pkl', 'rb'))

city_dict={'Goa': 3375,
 'New-Delhi': 3375,
 'Bangalore': 3375,
 'Mumbai': 2870,
 'Pune': 1774,
 'Kolkata': 1652}

city_selected = st.selectbox(
    'Enter the city you want to visit ?',
    tuple(city_dict.keys()),
    index=None,
    placeholder="Select city..."
)

staydays = st.number_input('Input the no of days you are going to stay', min_value=0, max_value=15, value=0, step=1)


city_input = city_dict.get(city_selected, 0) if city_selected is not None else 0

if st.button("Predict", type="primary"):
    input_data = np.array([city_input, staydays])
    
    input_data = input_data.astype(float)  
    
    prediction = model.predict(input_data.reshape(1, -1))
    predicted_price = prediction[0]
    st.write('The predicted price for the hotel is: Rs. {:.2f}'.format(prediction[0]*81))
    tolerance = 100
    
    min_price = predicted_price - tolerance
    max_price = predicted_price + tolerance
    df['price'] = pd.to_numeric(df['price'], errors='coerce')
    
    recommended_hotels = df[(df['city'] == city_selected) & 
                            (df['price'] >= min_price) & 
                            (df['price'] <= max_price)].drop(columns=['price','stay']).sample(5)
    
    st.subheader('Recommended hotels in the city as per budget range:')
    st.write(recommended_hotels)
