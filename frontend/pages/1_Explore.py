import streamlit as st
import requests
import json

def playSong(filename):
    backend_url = f'http://localhost:8080/api/song/download/{filename}'
    response = requests.get(backend_url)
    
    if response.status_code == 200:
        st.audio(response.content, format='audio/mp3')
    else:
        st.error("Failed to fetch MP3 file from the backend")

col1, col2, col3 = st.columns([1,2,1])
col2.image("riff_logo.png")

if st.session_state["login"] == "false":
    st.write("Please login first!")
    if st.button("Login"):
        st.switch_page('app.py')

else:
    url = 'http://localhost:8080/api/song/getAll'
    response = requests.get(url)
    if response.status_code == 200:
        song_list = response.content.decode('utf-8').replace("'",'"')
        for song in json.loads(song_list):
            if st.button(f"{song['title']} By {song['artist']} - ({song['genre']}, {song['duration']})"):
                filename = song['filename']
                playSong(filename)