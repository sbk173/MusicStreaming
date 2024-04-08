import streamlit as st
import requests
import base64
from collections import deque

col1, col2, col3 = st.columns([1, 2, 1])
col2.image("riff_logo.png")

BASE_URL = "http://localhost:8080/api/playlist"
song_queue = deque()

def playSong():
    if song_queue:
        st.header("Playing Playlist")
        while song_queue:
            filename = song_queue.popleft()
            backend_url = f'http://localhost:8080/api/song/download/{filename}'
            response = requests.get(backend_url)
            if response.status_code == 200:
                audio_data = response.content
                audio_base64 = base64.b64encode(audio_data).decode('utf-8')
                audio_html = f'<audio autoplay controls><source src="data:audio/mp3;base64,{audio_base64}" type="audio/mp3"></audio>'
                st.markdown(audio_html, unsafe_allow_html=True)

            else:
                st.error("Failed to fetch MP3 file from the backend")
    else:
        st.warning("No songs in the queue.")


def main():
    if st.session_state.get("login") == "false":
        st.write("Please login first!")
        if st.button("Login"):
            st.switch_page('app.py')
        return
    
    if st.session_state.get("actor") == "Artist":
        st.write("Please login as a user!")
        if st.button("Login"):
            st.session_state["login"] = "false"
            st.switch_page('app.py')
        return
    
    st.title("Your Playlists")

    selected_feature = st.selectbox("", ["Create Playlist", "Get Playlist", "All Playlists", "Add User to Playlist"])

    if selected_feature == "Create Playlist":
        create_playlist()
    elif selected_feature == "Get Playlist":
        get_playlist()
    elif selected_feature == "All Playlists":
        get_all_playlists()
    elif selected_feature == "Add User to Playlist":
        add_user_to_playlist()

def create_playlist():
    st.header("Create a New Playlist")
    playlist_name = st.text_input("Playlist Name")
    if st.button("Create Playlist"):
        payload = {"name": playlist_name}
        response = requests.post(f"{BASE_URL}/addPlaylist", json=payload)
        if response.status_code == 201:
            st.success("Playlist created successfully!")
        else:
            st.error("Failed to create playlist.")

def get_playlist():
    st.header("Get Playlist by ID")
    playlist_id = st.text_input("Enter Playlist ID")

    if st.button("Get Playlist"):
        response = requests.get(f"{BASE_URL}/getPlaylistById/{playlist_id}")
        if response.status_code == 200:
            playlist = response.json()
            st.write(f"Playlist ID: {playlist['id']}, Name: {playlist['name']}")
            play_id = playlist['id']
            display_songs_in_playlist(play_id)
        else:
            st.error("Failed to fetch playlist.")



def display_songs_in_playlist(play_id):
    playlist_data = requests.get(f"{BASE_URL}/getAllPlaylists").json()
    st.write(playlist_data)
    st.header("Songs in Playlist")
    for playlist in playlist_data:
        playlist_id = playlist["id"]
        songs = playlist["songs"]
        if playlist_id == play_id:
            if songs:
                st.write(f"Playlist ID: {playlist_id}")
                for song in songs:
                    song_title = song.get("title", "N/A")
                    song_artist = song.get("artist", "N/A")
                    song_genre = song.get("genre", "N/A")
                    song_duration = song.get("duration", "N/A")
                    filename = song.get("filename", "N/A")
                    song_queue.append(filename)
                    st.write(
                        f"Title: {song_title}, Artist: {song_artist}, Genre: {song_genre}, Duration: {song_duration}"
                    )
                st.button("Play Playlist", on_click=playSong)  # Use on_click to call playSong
            else:
                st.warning("No songs found in the playlist.")


def get_all_playlists():
    st.header("All Playlists")
    response = requests.get(f"{BASE_URL}/getAllPlaylists")
    if response.status_code == 200:
        playlists = response.json()
        for playlist in playlists:
            st.write(f"Playlist ID: {playlist['id']}, Name: {playlist['name']}")
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

if __name__ == "__main__":
    main()

