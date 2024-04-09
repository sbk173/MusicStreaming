import streamlit as st
import requests
import base64

def playSong(filename):
    backend_url = f'http://localhost:8080/api/song/download/{filename}'
    response = requests.get(backend_url)
    if response.status_code == 200:
        audio_data = response.content
        audio_base64 = base64.b64encode(audio_data).decode('utf-8')
        audio_html = f'<audio autoplay controls><source src="data:audio/mp3;base64,{audio_base64}" type="audio/mp3"></audio>'
        st.markdown(audio_html, unsafe_allow_html=True)
        st.session_state["currently_playing"] = filename
    else:
        st.error("Failed to fetch MP3 file from the backend")

def add_to_playlist(song_id, song_title, song_artist, song_genre, song_duration, song_filename, playlist_id):
    if playlist_id:
        response = requests.post(f"http://localhost:8080/api/playlist/{playlist_id}/addSong/{song_id}", json={
            "song_title": song_title,
            "song_artist": song_artist,
            "song_genre": song_genre,
            "song_duration": song_duration,
            "song_filename": song_filename
        })
        if response.status_code == 200:
            st.success(f"'{song_title}' by '{song_artist}' added to playlist successfully!")
        else:
            st.error("Failed to add song to playlist.")
    else:
        st.warning("Please enter a playlist ID.")

col1, col2, col3 = st.columns([1, 2, 1])
col2.image("riff_logo.png")

if st.session_state.get("login") == "false":
    st.write("Please login first!")
    if st.button("Login"):
        st.switch_page('app.py')

elif st.session_state.get("actor") == "Artist":
        st.write("Please login as a user!")
        if st.button("Login"):
            st.session_state["login"] = "false"
            st.switch_page('app.py')
    
else:
    url = 'http://localhost:8080/api/song/getAll'
    response = requests.get(url)
    if response.status_code == 200:
        song_list = response.json()
        for index, song in enumerate(song_list):
            song_id = song['id']
            song_title = song['title']
            song_artist = song['artist']
            song_genre = song['genre']
            song_duration = song['duration']
            song_filename = song['filename']
            col1, col2, col3 = st.columns([1, 10, 1])
            with col2:
                play_button_key = f"play_button_{index}"
                if st.button(f"{song_title} By {song_artist} - ({song_genre}, {song_duration})", key=play_button_key):
                    playSong(song_filename)
                # Check if the currently playing song is the same as the song in this iteration
                if st.session_state.get("currently_playing") == song_filename:
                    add_button_key = f"add_button_{index}"
                    playlist_id = st.text_input("Enter Playlist ID", key=f"playlist_input_{index}")
                    if st.button("Add to Playlist", key=add_button_key):
                        add_to_playlist(song_id, song_title, song_artist, song_genre, song_duration, song_filename, playlist_id)
            st.write("---")
    else:
        st.error("Failed to fetch song list.")