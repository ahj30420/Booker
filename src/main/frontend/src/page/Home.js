import React from "react";
import BestSeller from "../component/BestSeller";

function Home() {
    const bestSellers = [
        { rank: "1", key: "1" },
        { rank: "2", key: "2" },
        { rank: "3", key: "3" },
        { rank: "4", key: "4" },
        { rank: "5", key: "5" },
    ];

    return (
        <div>
            <div className="banner">Img</div>
            <div className="bsContainerTitle">
                이달의 <br />
                베스트 셀러
            </div>
            <div className="bsContainer">
                {bestSellers.map((seller) => (
                    <BestSeller key={seller.key} Rank={seller.rank} />
                ))}
            </div>
        </div>
    );
}

export default Home;