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

jest.mock("axios");

const resize = (width: number) => {
  Object.defineProperty(window, "innerWidth", {
    get: () => width,
  });

  Object.defineProperty(window.HTMLElement.prototype, "offsetWidth", {
    get: () => width,
  });

  window.dispatchEvent(new Event("resize"));
};

describe("Carousel", () => {
  beforeAll(() => resize(500)); // mobile

  test("no listing found", async () => {
    render(
      <BrowserRouter>
        <RecommendedCarousel />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(screen.getByText("No recommendations found")).toBeInTheDocument();
    });
  });

  test("one listing", async () => {
    (axios.get as jest.Mock).mockResolvedValue({
      data: [
        {
          title: "Test Card",
          listingId: 1,
          author: "test",
          price: "test",
          alt: "test",
        },
      ],
    });
    render(
      <UserLoginContext.Provider
        value={{
          isLoggedIn: true,
          jwtToken: "state.jwtToken",
          profile: { name: "kevin", id: "1", email: "email", isSeller: "true" },
          logOut: () => {},
          logIn: () => {},
          setToken: () => {},
          setProfile: () => {},
        }}
      >
        <BrowserRouter>
          <RecommendedCarousel />
        </BrowserRouter>
      </UserLoginContext.Provider>
    );

    await waitFor(async () => {
      const listings = await screen.findAllByText("Test Card");
      expect(listings).toHaveLength(1);
    });
  });
});
