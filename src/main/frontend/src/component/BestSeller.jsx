import React from "react";
import { Link } from "react-router-dom";
import "../css/BestSeller.css";

function BestSeller(props) {
    return (
        <div className="bsCard">
            <div className="bsRank">{props.Rank}</div>
            <div className="bsCardContent"></div>
        </div>
    );
}
export default BestSeller;