// import dependencies
import React from "react";
// import react-testing methods
import { render, waitFor, screen } from "@testing-library/react";
// add custom jest matchers from jest-dom
import "@testing-library/jest-dom";
import axios from "axios";
// the component to test
import ListingGrid from "src/components/Listing/ListingGrid";

jest.mock("axios");
const mockUseLocationValue = {
  pathname: "/testroute",
  search: "",
  hash: "",
  state: null,
};
jest.mock("react-router", () => ({
  ...(jest.requireActual("react-router") as {}),
  useLocation: jest.fn().mockImplementation(() => {
    return mockUseLocationValue;
  }),
}));
test("no listing found", async () => {
  render(<ListingGrid listings={[]} />);

  await waitFor(() => {
    expect(screen.getByText("No listings found")).toBeInTheDocument();
  });
});

test("one listing", async () => {
  (axios.get as jest.Mock).mockResolvedValue({
    data: [
      {
        cardName: "Test Card",
        listingId: 1,
        author: "test",
        price: "test",
        alt: "test",
      },
    ],
  });
  const res = await axios.get("/listing");
  render(<ListingGrid listings={res.data} />);

  await waitFor(async () => {
    const listings = await screen.findAllByText("Test Card");
    expect(listings).toHaveLength(1);
  });
});
