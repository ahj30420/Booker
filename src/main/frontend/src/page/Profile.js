import React, { useRef, useState } from "react";
import Category from "../component/Category";
import { TextField } from "@material-ui/core";
import Avatar from "@mui/material/Avatar";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

import "../css/Profile.css";

function Profile() {
    const navigate = useNavigate();
    const { idx } = useParams();
    const [nickName, setNickName] = useState("");
    const [introduction, setIntroduction] = useState("");
    const [Image, setImage] = useState("");
    const [previewImage, setPreviewImage] = useState(null);

    const fileInput = useRef(null);
    const onChange = (e) => {
        if (e.target.files[0]) {
            setImage(e.target.files[0]);
            const reader = new FileReader();
            reader.onload = () => {
                setPreviewImage(reader.result);
            };
            reader.readAsDataURL(e.target.files[0]);
        }   else {
            //업로드 취소할 시
            setImage(Image);
            setPreviewImage(null);
            return;
        }
        //화면에 프로필 사진 표시
        // const reader = new FileReader();
        // reader.onload = () => {
        //     if (reader.readyState === 2) {
        //         setImage(reader.result);
        //     }
        // };
        // reader.readAsDataURL(e.target.files[0]);
    };
    const habdleNickName = (e) => {
        setNickName(e.target.value);
    };
    const handleIntroduction = (e) => {
        setIntroduction(e.target.value);
    };

    const categoryList = [
        "백과사전",
        "수필집",
        "시집",
        "철학",
        "경제/경영",
        "정치",
        "수학",
        "천문학",
        "만화",
        "언어",
        "백과사전1",
        "수필집1",
        "시집1",
        "철학1",
        "경제/경영1",
        "정치1",
        "수학1",
        "천문학1",
        "만화1",
        "언어1",
    ];

    const [selectedCategories, setSelectedCategories] = useState([]);

    const handleCategorySelect = (categoryName) => {
        if (selectedCategories) {
            if (selectedCategories.length < 5) {
                setSelectedCategories((prevSelected) => {
                    const isSelected = prevSelected.includes(categoryName);

                    if (isSelected) {
                        // 이미 선택된 경우, 해당 카테고리를 리스트에서 제거
                        return prevSelected.filter((category) => category !== categoryName);
                    } else {
                        // 선택되지 않은 경우, 최대 5개까지 추가
                        return [...prevSelected, categoryName];
                    }
                });
            } else {
                // 이미 5개가 선택된 경우, 선택을 추가하지 않음
                if (!selectedCategories.includes(categoryName)) {
                    window.alert("카테고리의 선택 가능 개수는 최대 5개 입니다.");
                }
                setSelectedCategories((prevSelected) =>
                    prevSelected.includes(categoryName)
                        ? prevSelected.filter((category) => category !== categoryName)
                        : prevSelected
                );
            }
        }
    };

    const onProfilehandle = async (e) => {
        const [interest1, interest2, interest3, interest4, interest5] =
            selectedCategories;

        const formData = new FormData();
        formData.append("idx", idx);
        if(Image) {
            formData.append("imageFile", Image);
        }
        formData.append("nickname", nickName);
        formData.append("intro", introduction);
        formData.append("interest1",interest1);
        formData.append("interest2",interest2);
        formData.append("interest3",interest3);
        formData.append("interest4",interest4);
        formData.append("interest5",interest5);
        await axios
            .post("/profile", formData)
            .then((response) => {
                console.log(response.data);
                window.alert("프로필 설정 완료");
                navigate("/login");
            })
            .catch((error) => {
                console.log(error);
            });
        console.log(
            Image,
            nickName,
            introduction,
            interest1,
            interest2,
            interest3,
            interest4,
            interest5
        );
    };
    return (
        <div>
            <div className="profile">
                <div className="profileLeft">
                    <div className="profileImg">
                        <Avatar
                            className="profileImgSelect"
                            src={previewImage || Image}
                            style={{
                                backgroundPosition: "center",
                                backgroundRepeat: "no-repeat",
                                backgroundSize: "contain",
                            }}
                            sx={{ width: 330, height: 330 }}
                            onClick={() => {
                                fileInput.current.click();
                            }}
                        />
                        <input
                            type="file"
                            style={{ display: "none" }}
                            accept="image/jpg,impge/png,image/jpeg"
                            name="profile_img"
                            onChange={onChange}
                            ref={fileInput}
                        />
                    </div>
                    <div className="nickName">
                        <span>닉네임</span>
                        <br />
                        <span>사용자님의 닉네임을 작성해주세요.</span>
                    </div>
                    <div className="pro_inputWrap">
                        <TextField
                            className="input"
                            label="NICKNAME"
                            value={nickName}
                            type="text"
                            placeholder="사용하실 닉네임을 입력해주세요"
                            InputProps={{
                                disableUnderline: true,
                            }}
                            onChange={habdleNickName}
                        ></TextField>
                    </div>
                    <div className="introduction">
                        <span>소개글</span>
                        <br />
                        <span>본인을 소개하는 간단한 소개글을 작성해주세요.</span>
                    </div>
                    <div className="pro_inputWrap">
                        <TextField
                            className="input"
                            label="INTRODUCTION"
                            value={introduction}
                            type="text"
                            placeholder="본인을 소개하는 간단한 글 작성해주세요."
                            InputProps={{
                                disableUnderline: true,
                            }}
                            onChange={handleIntroduction}
                        ></TextField>
                    </div>
                </div>
                <div className="profileRight">
                    <div className="categorySection">
                        <span>관심 분야 선택</span>
                        <br />
                        <span>본인의 관심 카테고리를 알려주세요!</span>
                        <div className="categorySelector">
                            {categoryList.map((categoryName, index) => (
                                <Category
                                    key={index}
                                    Category={categoryName}
                                    onClick={() => handleCategorySelect(categoryName)}
                                    isSelected={selectedCategories.includes(categoryName)}
                                />
                            ))}
                        </div>
                    </div>
                    <div className="categoryTotal">
                        <span>선택된 카테고리</span>
                        <div className="selectedCategories">
                            {selectedCategories.map((category, index) => (
                                <Category key={index} Category={category} />
                            ))}
                        </div>
                    </div>
                    <div className="proBtnWrap">
                        <button className="profile_btn" onClick={onProfilehandle}>
                            확인
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
export default Profile;