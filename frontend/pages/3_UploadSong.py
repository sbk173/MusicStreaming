import streamlit as st
import requests
import json
import mutagen
from mutagen.mp3 import MP3

def audio_duration(length):
    hours = length//3600
    length %= 3600
    minutes = length//60
    length %= 60
    seconds = length
    return hours, minutes, seconds

def switch_login_state():
    if st.session_state["login"] == "false":
        st.session_state["login"] = "true"
    else:
        st.session_state["login"] = "false"
        st.session_state["actor"] = "guest"
        st.session_state["user_id"] = 0

col1, col2, col3 = st.columns([1,2,1])
col2.image("riff_logo.png")

if st.session_state["login"] == "false":
    st.write("Please login first!")
    if st.button("Login"):
        st.switch_page('Riff.py')

elif st.session_state["actor"] == "User":
    st.write("Please signout and login as an artist to upload songs!")
    if st.button("Signout"):
        switch_login_state()
        st.switch_page('Riff.py')

else:
    user_id = st.session_state["user_id"]
    actor = st.session_state["actor"]
    url = f"http://localhost:8080/api/{actor.lower()}/profile/{user_id}"
    response = requests.get(url)
    artist_profile = json.loads(response.content.decode('utf-8').replace("'",'"'))

    uploaded_song = st.file_uploader("Upload MP3 songs", type=['mp3'])
    if uploaded_song is not None:
        title =  st.text_input("Song Title")
        genre =  st.text_input("Genre")
        artist = artist_profile["username"]
        file_name =  uploaded_song.name
        song = MP3(uploaded_song)
        uploaded_song.seek(0)
        duration = int(song.info.length)
        hours, minutes, seconds = audio_duration(duration)
        st.write("Song_duration = {}:{}:{}".format(hours, minutes, seconds))
        url = 'http://localhost:8080/api/song/upload'
        files = {}
        files["file"] = uploaded_song
        data ={"title":title,
               "artist":artist,
               "genre":genre,
               "duration": duration,
               "filename":file_name,
               "artistid": user_id}
        if st.button("Upload"):
            response = requests.post(url, files=files,data={"data":json.dumps(data)})
            if response.status_code == 201:
                st.success("File uploaded successfully!")
            else:
                st.error("Error uploading file.")