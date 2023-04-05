// import dependencies
import React from "react";
// import react-testing methods
import { render, waitFor, screen } from "@testing-library/react";
// add custom jest matchers from jest-dom
import "@testing-library/jest-dom";
import axios from "axios";
// the component to test
import RecommendedCarousel from "src/components/Recommended/RecommendedCarousel";
import { UserLoginContext } from "src/context/userLoginContext";
import { BrowserRouter } from "react-router-dom";
import AllListings from "../AllListings";

jest.mock("axios");

describe("All Listings", () => {

    test("no listing found", async () => {
        (axios.get as jest.Mock).mockResolvedValue({
            data: [{
                cardName: "Test Card",
                listingId: 1,
                author: "test",
                price: "test",
                alt: "test"
            }]
        })
        const res = await axios.get('/listing');
        const {container} =
            render(
                <BrowserRouter>
                    <AllListings> </AllListings>
                </BrowserRouter>
         );
        expect(container.querySelector("input")).toBeDefined();
    });
});
