import streamlit as st
import requests
import json
import time
import base64

def playSong(filename):
    backend_url = f'http://localhost:8080/api/song/download/{filename}'
    response = requests.get(backend_url)
    
    if response.status_code == 200:
        # st.audio(response.content, format='audio/mp3')
        audio_data = response.content
        audio_base64 = base64.b64encode(audio_data).decode('utf-8')
        audio_html = f'<audio autoplay controls><source src="data:audio/mp3;base64,{audio_base64}" type="audio/mp3"></audio>'
        st.markdown(audio_html, unsafe_allow_html=True)
    else:
        st.error("Failed to fetch MP3 file from the backend")

col1, col2, col3 = st.columns([1,2,1])
col2.image("riff_logo.png")

if st.session_state["login"] == "false":
    st.write("Please login first!")
    if st.button("Login"):
        st.switch_page('app.py')

else:
    context = st.text_input("Search videos by title or artist", value="")
    if context != "":
        url = f'http://localhost:8080/api/song/search/{context}'
        response = requests.get(url)
    else:
        url = 'http://localhost:8080/api/song/getAll'
        response = requests.get(url)
    if response.status_code == 200:
        song_list = response.content.decode('utf-8').replace("'",'"')
        for song in json.loads(song_list):
            if st.button(f"{song['title']} By {song['artist']} - ({song['genre']}, {song['duration']})"):
                filename = song['filename']
                playSong(filename)