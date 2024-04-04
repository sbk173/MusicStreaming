import streamlit as st
import requests
import json
import time

if "login" not in st.session_state:
    st.session_state["login"] = "false"
if "actor" not in st.session_state:
    st.session_state["actor"] = "guest"
if "user_id" not in st.session_state:
    st.session_state["user_id"] = 0

def switch_login_state():
    if st.session_state["login"] == "false":
        st.session_state["login"] = "true"
    else:
        st.session_state["login"] = "false"
        st.session_state["actor"] = "guest"
        st.session_state["user_id"] = 0

def header():
    col1, col2, col3, col4, col5 = st.columns([1,2,3,1,2])
    col3.image("riff_logo.png")
    col5.button("Signout", on_click=switch_login_state)
    if st.session_state["login"] == "true":
        Profile()
        
def Profile():
    st.subheader("My Profile")
    profile_picture = st.checkbox("Take Picture")
    with st.expander("See Yourself"):
        if profile_picture:
            picture = st.camera_input("New Day, New Me!")
        actor = st.session_state["actor"]
        user_id = st.session_state["user_id"]
        url = f"http://localhost:8080/api/{actor.lower()}/profile/{user_id}"
        response = requests.get(url)
        if response.status_code == 200:
            user_profile = json.loads(response.content.decode('utf-8').replace("'",'"'))
            for i in user_profile:
                st.write(i + ": " + str(user_profile[i]))

def login_section():
    st.subheader("Login")
    username = st.text_input("Username")
    password = st.text_input("Password", type="password")
    actor = st.radio("Actor", ["User", "Artist"])
    if st.button("Login"):
        url = f"http://localhost:8080/api/{actor.lower()}/login/{username}/{password}"
        response = requests.get(url)
        status_code = response.status_code
        if status_code == 200:
            st.success("Login successful!")
            user_data = response.content.decode('utf-8').replace("'",'"') #Fix for JSON decoding error
            user_id = json.loads(user_data)['id']
            st.session_state["actor"] = actor
            st.session_state["user_id"] = user_id
            time.sleep(5)
            return True
        else:
            st.error("Invalid username or password. Please try again.")
    return False
    
def register_section():
    st.subheader("Register")
    email = st.text_input("Email")
    username = st.text_input("Username")
    password = st.text_input("Password", type="password")
    actor = st.radio("Actor", ["User", "Artist"])
    actor_info = {"email": email, "password": password, "username": username}
    if st.button("Register"):
        url = f'http://localhost:8080/api/{actor.lower()}/register'
        response = requests.post(url, data=json.dumps(actor_info), headers={"Content-Type": "application/json"})
        status_code = response.status_code
        if status_code == 201:
            st.success(f"{actor} Successfully Registered!")
            time.sleep(5)
            return True
        else:
            st.error(f"Registration Failed. Try again later {status_code}")
    return False


def main():
    if st.session_state["login"] == "false":
        placeholder = st.empty()
        with placeholder.container():
            col1, col2, col3 = st.columns([1,2,1])
            col2.image("riff_logo.png")
            page = st.radio("Access", ["Login","Register"], index=0)
            if page == "Login":
                x = login_section()
            else:
                x = register_section()
            if x == True and page == "Login":
                placeholder.empty()
                switch_login_state()
    if st.session_state["login"] == "true":
        header()

if __name__ == "__main__":
    main()