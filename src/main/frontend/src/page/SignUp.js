import { TextField } from "@material-ui/core";
import React from "react";
import { lazy, Suspense, createContext, useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function SignUp() {
    const navigate = useNavigate();
    /*회원 정보*/
    const [id, setId] = useState("");
    const [pw, setPw] = useState("");
    const [pwCheck, setPwCheck] = useState("");
    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [birth, setBirth] = useState("");

    // const [idValid, setIdValid] = useState(false);
    // const [pwValid, setPwValid] = useState(false);
    const [notAllow, setNotAllow] = useState(true);

    const handleId = (e) => {
        setId(e.target.value);
    };
    const handlePw = (e) => {
        setPw(e.target.value);
        setPwTouched(true);
    };
    const handlePwCheck = (e) => {
        setPwCheck(e.target.value);
    };
    const handleEmail = (e) => {
        setEmail(e.target.value);
        setEmailTouched(true);
    };
    const handleName = (e) => {
        setName(e.target.value);
    };
    const handleBirth = (e) => {
        let inputValue = e.target.value;

        // 입력된 텍스트를 형식에 맞게 수정
        inputValue = inputValue.replace(/[^0-9-]/g, ""); // 숫자와 '-' 이외의 문자 제거

        // 생년월일 형식에 맞게 추가
        if (inputValue.length >= 5 && inputValue.charAt(4) !== "-") {
            inputValue = inputValue.slice(0, 4) + "-" + inputValue.slice(4);
        }

        if (inputValue.length >= 8 && inputValue.charAt(7) !== "-") {
            inputValue = inputValue.slice(0, 7) + "-" + inputValue.slice(7);
        }

        setBirth(inputValue);
        setBirthTouched(true);
    };
    //비밀번호 길이 체크
    const [pwLengthErr, setPwLengthErr] = useState(false);
    const [pwTouched, setPwTouched] = useState(false);
    const hasNotPwLength = (passwordEntered) => (pw.length < 8 ? true : false);
    useEffect(() => {
        if (pwTouched) {
            setPwLengthErr(hasNotPwLength(pw));
        }
    }, [pw, pwTouched]);

    //비밀번호 확인
    const hasNotPwSame = (passwordEntered) => (pw != pwCheck ? true : false);

    //이메일 형식
    const isEmailValid = (email) => {
        // 간단한 이메일 정규식을 사용하여 형식 검증
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    };
    const [emailFormErr, setEmailFormErr] = useState(false);
    const [emailTouched, setEmailTouched] = useState(false);
    useEffect(() => {
        if (emailTouched) {
            setEmailFormErr(isEmailValid(email));
        }
    }, [email, emailTouched]);

    //생년월일 글자수
    const [birthLengthErr, setBirthLengthErr] = useState(false);
    const [birthTouched, setBirthTouched] = useState(false);
    const BirthLength = (BirthEntered) => (birth.length != 10 ? true : false);
    useEffect(() => {
        if (birthTouched) {
            setBirthLengthErr(BirthLength(birth));
        }
    }, [birth, birthTouched]);

    //확인 버튼 활성화
    useEffect(() => {
        if (id && pw && pwCheck && email && name && birth) {
            setNotAllow(false);
            return;
        }
        setNotAllow(true);
    }, [id, pw, pwCheck, email, name, birth]);

    const onSubmithandle = async (e) => {
        e.preventDefault();
        await axios
            .post("/signup", {
                id: id,
                pw: pw,
                email: email,
                name: name,
                birth: birth,
            })
            .then((response) => {
                window.alert("회원가입 완료");
                const idx = response.data.idx;
                navigate(`/Profile/${idx}`);
                setId("");
                setPw("");
                setEmail("");
                setName("");
                setBirth("");
            })
            .catch((error) => {
                console.log(error);
            });
    };
    return (
        <div className="signUpContent">
            <div className="titleWrap">
                회원가입
                <br />
                정보를 입력해 주세요
            </div>
            <div className="SingUp">
                <div className="inputWrap">
                    <TextField
                        className="input"
                        label="ID"
                        value={id}
                        placeholder="ID 입력"
                        InputProps={{
                            disableUnderline: true,
                        }}
                        autoComplete="currentID"
                        onChange={handleId}
                    ></TextField>
                </div>

                <div className="inputWrap">
                    <TextField
                        error={pwTouched && pwLengthErr}
                        helperText={
                            pwTouched && pwLengthErr ? "비밀번호는 8자리 이상." : null
                        }
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

                <div className="inputWrap">
                    <TextField
                        error={hasNotPwSame("pwCheck")}
                        helperText={
                            hasNotPwSame("pwCheck")
                                ? "입력하신 비밀번호가 일치하지 않습니다."
                                : null
                        }
                        className="input"
                        label="PASSWORD *"
                        value={pwCheck}
                        type="password"
                        placeholder="비밀번호 확인"
                        InputProps={{
                            disableUnderline: true,
                        }}
                        onChange={handlePwCheck}
                    ></TextField>
                </div>

                <div className="inputWrap">
                    <TextField
                        error={emailTouched && !emailFormErr}
                        helperText={
                            emailTouched && !emailFormErr
                                ? "이메일 형식으로 작성해주세요"
                                : null
                        }
                        className="input"
                        label="Email"
                        value={email}
                        type="Email"
                        placeholder="비밀번호 분실 시 확인용 이메일"
                        InputProps={{
                            disableUnderline: true,
                        }}
                        onChange={handleEmail}
                    ></TextField>
                </div>
                <div className="inputWrap">
                    <TextField
                        className="input"
                        label="Name"
                        value={name}
                        type="text"
                        placeholder="이름"
                        InputProps={{
                            disableUnderline: true,
                        }}
                        onChange={handleName}
                    ></TextField>
                </div>

                <div className="inputWrap">
                    <TextField
                        error={birthTouched && birthLengthErr}
                        helperText={
                            birthTouched && birthLengthErr
                                ? "생년월일 8자리 입력해 주세요."
                                : null
                        }
                        className="input"
                        label="Birth"
                        value={birth}
                        type="text"
                        placeholder="생년월일 8자리"
                        maxLength
                        InputProps={{
                            disableUnderline: true,
                        }}
                        inputProps={{
                            maxLength: 10,
                        }}
                        onChange={handleBirth}
                    ></TextField>
                </div>
                <button
                    disabled={notAllow}
                    onClick={onSubmithandle}
                    className="signUp_btn"
                >
                    확인
                </button>
            </div>
        </div>
    );
}
export default SignUp;