import "./css/App.css";
import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./page/Login";
import SignUp from "./page/SignUp";
import Home from "./page/Home";
import Header from "./component/Header";
import Profile from "./page/Profile";

function App() {
    return (
        <Router>
            <Header />
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<SignUp />} />
                <Route path="/profile/:idx" element={<Profile />} />
                <Route path="/" element={<Home />} />
            </Routes>
        </Router>
    );
}

export default App;