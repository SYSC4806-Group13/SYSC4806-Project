import React from "react";
import { render, screen } from "@testing-library/react";
import ProtectedPage from "src/pages/ProtectedPage/ProtectedPage";
import { UserLoginContext } from "src/context/userLoginContext";

const mockedUsedNavigate = jest.fn();

jest.mock("react-router-dom", () => ({
  ...(jest.requireActual("react-router-dom") as any),
  useNavigate: () => mockedUsedNavigate,
}));

describe("Testing ProtectedPage", () => {
  it("renders the page", () => {
    render(<ProtectedPage />);
    const headerElement = screen.getByText(
      /This is a sample of an Authorized route/i
    );
    const paragraphElement = screen.getByText(
      /If you see this, you are loggedin/i
    );
    expect(headerElement).toBeInTheDocument();
    expect(paragraphElement).toBeInTheDocument();
  });

  it("renders the page with header logged in", () => {
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
        <ProtectedPage />
      </UserLoginContext.Provider>
    );

    const loginButton = screen.queryAllByTestId("login button");
    expect(loginButton).toHaveLength(0);
  });

  it("renders the page with header logged out", () => {
    render(
      <UserLoginContext.Provider
        value={{
          isLoggedIn: false,
          jwtToken: "state.jwtToken",
          profile: { name: "kevin", id: "1", email: "email", isSeller: "true" },
          logOut: () => {},
          logIn: () => {},
          setToken: () => {},
          setProfile: () => {},
        }}
      >
        <ProtectedPage />
      </UserLoginContext.Provider>
    );

    const loginButton = screen.getByTestId("login button");
    expect(loginButton).toBeInTheDocument();
  });
});
