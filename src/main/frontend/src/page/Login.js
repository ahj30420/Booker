import { TextField } from "@material-ui/core";
import React from "react";
import { lazy, Suspense, createContext, useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import axios from "axios";
function Login() {
    const navigate = useNavigate();

    const [id, setId] = useState("");
    const [pw, setPw] = useState("");

    // const [idValid, setIdValid] = useState(false);
    // const [pwValid, setPwValid] = useState(false);
    const [notAllow, setNotAllow] = useState(true);

    const handleId = (e) => {
        setId(e.target.value);
    };
    const handlePw = (e) => {
        setPw(e.target.value);
    };

    useEffect(() => {
        if (id && pw) {
            setNotAllow(false);
            return;
        }
        setNotAllow(true);
    }, [id, pw]);

    const loginhandle = async (e) => {
        e.preventDefault();
        await axios
            .post("/login", {
                id: id,
                pw: pw,
            })
            .then((response) => {
                navigate("/Main");
                console.log(response.data);
                localStorage.setItem("accesstoken", response.data.accessToken);
                localStorage.setItem("refreshtoken", response.data.refreshToken);
            })
            .catch((error) => {
                console.log(error);
            });
    };

    return (
        <div className="loginContent">
            <div className="titleWrap">
                아이디와 비밀번호를
                <br />
                입력해주세요
            </div>
            <div className="Login">
                <div className="inputWrap">
                    <TextField
                        className="input"
                        label="ID"
                        value={id}
                        placeholder="ex) lgs2260@naver.com"
                        InputProps={{
                            disableUnderline: true,
                        }}
                        onChange={handleId}
                    ></TextField>
                </div>
                <div className="loginErr">올바른 아이디를 입력해주세요</div>

                <div className="inputWrap">
                    <TextField
                        className="input"
                        label="PASSWORD"
                        value={pw}
                        type="password"
                        placeholder="영문,숫자,특수문자 포함 8자 이상"
                        InputProps={{
                            disableUnderline: true,
                        }}
                        onChange={handlePw}
                    ></TextField>
                </div>
            </div>
            <div>
                <button disabled={notAllow} onClick={loginhandle} className="login_btn">
                    로그인
                </button>
                <button className="login_btn Naver">네이버</button>
                <button className="login_btn Google">
                    <img
                        className="google"
                        src="https://www.google.co.kr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"
                    ></img>
                </button>
            </div>
            <ul className="findWrap">
                <li>
                    <a>비밀번호 찾기</a>
                </li>
                <li>
                    <a>아이디 찾기</a>
                </li>
                <li>
                    <Link to="/SignUp" className="SignUp_btn">
                        회원가입
                    </Link>
                </li>
            </ul>
        </div>
    );
}

export default Login;