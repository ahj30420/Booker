import React from "react";
import { Link } from "react-router-dom";

function Header() {
    return (
        <div className="header">
            <Link to="/" className="logo">
                로고
            </Link>
            <Link to="/Login">
                <button className="headerLogin_btn">로그인</button>
            </Link>
        </div>
    );
}
export default Header;