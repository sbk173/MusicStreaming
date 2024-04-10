import streamlit as st
import requests
import base64
from collections import deque
import time
import pandas as pd

song_queue = deque()

if "Playing_audio" not in st.session_state:
    st.session_state["Playing_audio"] = True
if "Audio_complete" not in st.session_state:
    st.session_state["Audio_complete"] = False

def create_playlist():
    st.header("Create a New Playlist")
    playlist_name = st.text_input("Playlist Name")
    if st.button("Create Playlist"):
        user_id = st.session_state["user_id"]
        payload = {"name": playlist_name,"users":[{"id":user_id}]}
        response = requests.post(f"{BASE_URL}/addPlaylist", json=payload)
        if response.status_code == 201:
            st.success("Playlist created successfully!")
        else:
            st.error("Failed to create playlist.")

def get_my_playlists():
    st.header("My Playlists")
    user_id = st.session_state["user_id"]
    response = requests.get(f"{BASE_URL}/getPlaylists/{user_id}")
    if response.status_code == 200:
        playlists = response.json()
        df = [{"Playlist id": playlist['id'], "Name": playlist['name']} for playlist in playlists]
        df = pd.DataFrame(df)
        st.dataframe(df.set_index(df.columns[0]),width=500)
    else:
        st.error("Failed to fetch playlists.")

def add_user_to_playlist():
    st.header("Add User to Playlist")
    playlist_id_user = st.text_input("Your Playlist ID")
    user_id = st.text_input("User ID")
    if st.button("Add User"):
        payload = {"playlistId": playlist_id_user, "userId": user_id}
        response = requests.post(f"{BASE_URL}/{playlist_id_user}/addUser/{user_id}")
        if response.status_code == 200:
            st.success("User added to playlist successfully!")
        else:
            st.error("Failed to add user to playlist.")

def play_song(filename):
    backend_url = f'http://localhost:8080/api/song/download/{filename}'
    response = requests.get(backend_url)
    if response.status_code == 200:
        audio_data = response.content
        audio_base64 = base64.b64encode(audio_data).decode('utf-8')
        audio_url = f'data:audio/mp3;base64,{audio_base64}'
        
        # Embedding custom HTML for audio player with autoplay
        audio_html = f'''
        <audio id="audio_player" controls autoplay>
            <source src="{audio_url}" type="audio/mp3">
            Your browser does not support the audio element.
        </audio>
        <script>
            // Wait for the audio player to be loaded
            document.getElementById("audio_player").onloadeddata = function() {{
                var audio = document.getElementById("audio_player");
                audio.onended = function() {{
                    // Notify Python that audio playback has ended
                    st.setComponentValue(True, "Audio_complete");
                }};
            }};
        </script>
        '''
        st.markdown(audio_html, unsafe_allow_html=True)
        
        return True

    else:
        st.error("Failed to fetch MP3 file from the backend")
        return False

@st.cache
def play_playlist(play_id):
    playlist_data = requests.get(f"{BASE_URL}/getSongs/{play_id}").json()

    for song in playlist_data:
        filename = song['filename']
        song_queue.append(filename)

    while st.session_state["Playing_audio"]:
        if song_queue:
            filename = song_queue.popleft()
            st.session_state["Audio_completed"] = False
            play_song(filename)
            while not st.session_state["Audio_completed"]:
                time.sleep(1)
            st.write(f"Song {filename} - playback complete")
        else:
            st.info("No songs in queue")
            st.session_state["Playing_audio"] = False
            time.sleep(3)
        

def display_songs_in_playlist(play_id):
    st.header("Songs in Playlist")
    songs = requests.get(f"{BASE_URL}/getSongs/{play_id}").json()
    if not songs:
        st.warning("No songs found in the playlist.")
        return

    st.subheader("All Songs in Playlist:")
    song_data = [{"Title": song['title'], "Artist": song['artist'], "Genre": song['genre'], "Duration": song['duration']} for song in songs]
    st.table(song_data)

    if st.button("Play Playlist", key="PlayPlaylist"):
        play_playlist(play_id)

def get_playlist():
    st.header("Get Playlist by ID")
    playlist_id = st.text_input("Enter Playlist ID")
    if playlist_id:
        response = requests.get(f"{BASE_URL}/getPlaylistById/{playlist_id}")
        if response.status_code == 200:
            playlist = response.json()
            st.write(f"Playlist ID: {playlist['id']}, Name: {playlist['name']}")
            play_id = playlist['id']
            display_songs_in_playlist(play_id)
        else:
            st.error("Failed to fetch playlist.")


st.image("riff_logo.png")

BASE_URL = "http://localhost:8080/api/playlist"

login_status = st.session_state.get("login")
actor = st.session_state.get("actor")

if login_status == "false":
    st.write("Please login first!")
    if st.button("Login"):
        st.switch_page('app.py')

elif actor == "Artist":
    st.write("Please login as a user!")
    if st.button("Login"):
        st.session_state["login"] = "false"
        st.switch_page('app.py')

else:
    st.title("Your Playlists")
    selected_feature = st.selectbox("", ["Create Playlist", "Get Playlist", "My Playlists", "Share Playlist"])
    if selected_feature == "Create Playlist":
        create_playlist()
    elif selected_feature == "Get Playlist":
        get_playlist()
    elif selected_feature == "My Playlists":
        get_my_playlists()
    elif selected_feature == "Share Playlist":
        add_user_to_playlist()