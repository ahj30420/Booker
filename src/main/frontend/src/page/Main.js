import { TextField } from "@material-ui/core";
import React from "react";
import { lazy, Suspense, createContext, useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function Main() {
    const [userId, setUserId] = useState();
    const [userPw, setUserPw] = useState();

    useEffect(() => {
        getUser();
    }, []);

    async function getUser() {
        await axios
            .get("")
            .then((response) => {
                console.log(response.data);
                setUserId(response.data.userId);
                setUserPw(response.data.userPw);
            })
            .catch((error) => {
                console.log(error);
            });
    }
    return (
        <div>
            <div></div>
        </div>
    );
}
export default Main;