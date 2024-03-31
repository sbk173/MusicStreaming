import streamlit as st
import requests
import json
# Function to upload file to backend
def upload_file_to_backend(file):
    url = 'http://localhost:8080/api/song/upload'
    files = {}
    data ={"id":"1",
           "title":"Top-G",
           "artist":"Top-G",
           "genre":"Pop",
           "duration":12,
           "filename":"ads"}
    files["file"] = file
    response = requests.post(url, files=files,data={"data":json.dumps(data)})
    if response.status_code == 201:
        st.success("File uploaded successfully!")
    else:
        st.error("Error uploading file.")

# Streamlit UI
def main():
    st.title("File Uploader")
    uploaded_file = st.file_uploader("Choose a file", type=['mp3'])

    if uploaded_file is not None:
        st.write("Filename:", uploaded_file.name)
        st.write("File type:", uploaded_file.type)
        st.write("File size (bytes):", uploaded_file.size)
        if st.button("Upload"):

            upload_file_to_backend(uploaded_file)

if __name__ == "__main__":
    main()