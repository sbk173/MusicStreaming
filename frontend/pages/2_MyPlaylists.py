import streamlit as st

col1, col2, col3 = st.columns([1,2,1])
col2.image("riff_logo.png")

if st.session_state["login"] == "false":
    st.write("Please login first!")
    if st.button("Login"):
        st.switch_page('app.py')