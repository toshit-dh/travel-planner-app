import streamlit as st
import os
import google.generativeai as genai
from io import BytesIO
from reportlab.lib.pagesizes import letter
from reportlab.platypus import SimpleDocTemplate, Paragraph
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from email.mime.base import MIMEBase
from email import encoders
import re


st.title("Trip-Planner")


os.environ['GOOGLE_API_KEY'] = "YOUR_API_KEY"
genai.configure(api_key = os.environ['GOOGLE_API_KEY'])

location=st.text_input("Enter the location you want to visit")
days=st.number_input("Enter the number of days you want to stay")
budget=st.number_input("Enter the budget you have")
email=st.text_input("Enter the email you want to send the itinerary to")

if "@" not in email:
    st.write("Please enter a valid email")
    st.stop()

input_prompt=f"""


You are a travel itinerary expert tasked with planning a trip for a traveler. The traveler has provided you with the following details:

- Destination: [Destination Name]
- Duration of Stay: [Number of Days]
- Budget: [Budget Amount]

Your task is to create a personalized travel itinerary for the traveler, suggesting places to visit and activities to do at the destination within the given constraints. Additionally, recommend some local attractions and experiences that the traveler might enjoy.

Your itinerary should include a list of suggested activities and attractions, as well as estimated costs for each activity. Be sure to consider the traveler's interests and preferences when planning the itinerary.

Once you have created the itinerary, present it in the following format:

1. Day 1:
   - Morning: [Activity/Attraction Name] - Cost: [Cost Estimate]
   - Afternoon: [Activity/Attraction Name] - Cost: [Cost Estimate]
   - Evening: [Activity/Attraction Name] - Cost: [Cost Estimate]
   - Total Daily Cost: [Total Cost for the Day]

2. Day 2:
   - ...

[Include details for each day of the trip, along with total costs for each day.]

Additionally, provide recommendations for other must-visit locations and interesting activities that the traveler can explore during their trip.


"""

input=f'Location: {location}, Duration of Stay: {days} days, Budget: {budget} USD'

model = genai.GenerativeModel('gemini-pro')


ans=''
if st.button("Generate Itinerary and Send Via Mail"):
    response = model.generate_content(f'Consider the following prompt: {input_prompt} and make a one like for{input}')
    st.write(response.text)
    ans=response.text
    
    i=1
    pdf_filename = f"example{i}.pdf"
    i+=1
    doc = SimpleDocTemplate(pdf_filename, pagesize=letter)
    
    content=[]
    for t in response.text.split("**"):
        content.append(Paragraph(t,))

    
    doc.build(content)

    print(f"PDF created successfully: {pdf_filename}")
    
    smtp_port = 587                 
    smtp_server = "smtp.gmail.com"  

    # Set up the email lists
    email_from = "YOUR_EMAIL"
    email_list = [email]

    
    pswd = "YOUR_EMAIL_KEY" 


    # name the email subject
    subject = "TravelPlan for you...By CodeCommandos..."


    def send_emails(email_list):

        for person in email_list:

            
            body = f"""
            Travel Plan for you....By Us...
            """

            
            msg = MIMEMultipart()
            msg['From'] = email_from
            msg['To'] = person
            msg['Subject'] = subject

            
            msg.attach(MIMEText(body, 'plain'))

            
            
            attachment= open(pdf_filename, 'rb')  

            # Encode as base 64
            attachment_package = MIMEBase('application', 'octet-stream')
            attachment_package.set_payload((attachment).read())
            encoders.encode_base64(attachment_package)
            attachment_package.add_header('Content-Disposition', "attachment; filename= " + pdf_filename)
            msg.attach(attachment_package)

            
            text = msg.as_string()

            print("Connecting to server...")
            TIE_server = smtplib.SMTP(smtp_server, smtp_port)
            TIE_server.starttls()
            TIE_server.login(email_from, pswd)
            print("Succesfully connected to server")
            print()


            
            print(f"Sending email to: {person}...")
            TIE_server.sendmail(email_from, person, text)
            print(f"Email sent to: {person}")
            print()
        
        TIE_server.quit()

    send_emails(email_list)



        







