import streamlit as st
import requests
from io import BytesIO

# Function to fetch and play MP3 file
def play_mp3_from_backend():
    backend_url = 'http://localhost:8080/api/song/download/16b99fb7-417e-4438-8ff3-12d30fd80d58_The_Lon_of_Chabu.mp3'
    response = requests.get(backend_url)
    st.write(response.content)
    
    if response.status_code == 200:
        st.audio(response.content, format='audio/mp3')
    else:
        st.error("Failed to fetch MP3 file from the backend")

def main():
    st.title("MP3 Player")
    st.write("Click the button below to play the MP3 file from the backend")
    if st.button("Play MP3"):
        play_mp3_from_backend()

if __name__ == "__main__":
    main()
