import React from "react";
import { Link } from "react-router-dom";
import "../css/Category.css";

function Category(props) {
    return (
        <div>
            <button
                className={`ctg_btn ${props.isSelected ? "selected" : ""}`}
                onClick={props.onClick}
            >
                <span>{props.Category}</span>
            </button>
        </div>
    );
}
export default Category;